const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_123";
const BOB_UID = "bob_456";

const [emulatorHost, emulatorPortStr] = (process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085").split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

test("Unauthenticated user: cannot read dreams", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).collection("dreams").get());
});

test("Unauthenticated user: cannot write dreams", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(
    unauthDb.collection("users").doc(ALICE_UID).collection("dreams").add({
      userId: ALICE_UID,
      title: "Test Dream",
      narrative: "Dream description",
      createdAt: new Date(),
      updatedAt: new Date()
    })
  );
});

test("Cross-user isolation: Bob cannot read Alice's dreams", async () => {
  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  await assertFails(bobDb.collection("users").doc(ALICE_UID).collection("dreams").get());
});

test("Cross-user isolation: Bob cannot write to Alice's dreams", async () => {
  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  await assertFails(
    bobDb.collection("users").doc(ALICE_UID).collection("dreams").add({
      userId: ALICE_UID,
      title: "Hacked Dream",
      narrative: "Unauthorized access",
      createdAt: new Date(),
      updatedAt: new Date()
    })
  );
});

test("Authenticated owner: Alice can create and read her own dream", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const dreamRef = aliceDb.collection("users").doc(ALICE_UID).collection("dreams").doc("dream_1");
  await assertSucceeds(
    dreamRef.set({
      userId: ALICE_UID,
      title: "The Floating City",
      narrative: "I was standing on a floating island...",
      mood: "Wonder",
      lucidityPercent: 85,
      createdAt: new Date(),
      updatedAt: new Date()
    })
  );
  await assertSucceeds(dreamRef.get());
});

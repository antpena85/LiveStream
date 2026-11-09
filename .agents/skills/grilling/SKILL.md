---
name: grilling
description: Grill the user relentlessly about a plan, decision, or idea. Use when the user wants to stress-test their thinking, or uses any 'grill' trigger phrases.
---

Interview the user relentlessly until you reach a shared understanding. Map this as a **design tree**: every decision branches into the decisions that hang off it.

Work the tree in **rounds**. The **frontier** is every decision whose prerequisites are already settled: the questions you can ask _now_ without guessing at answers you haven't heard yet. When the question interface permits one question at a time, ask one frontier question per call, with options and a recommended answer, then recompute the frontier after the answer. Follow project clarification rules and do not assume the recommendation was accepted.

Format each question like so, with selectable options as required by the project:

```
❓ **Q1** - **<question title>**: <question body and choices>

➡️ <your recommended answer>

```

Word each question so "yes" accepts your recommended answer.

Each round the user answers reshapes the tree: settled decisions push the frontier outward and unblock questions that depended on them. Recompute the frontier and ask the next round. A question whose answer depends on another question still open in this round belongs to a _later_ round, not this one.

Finding _facts_ is your job, never the user's. Investigate the environment using available tools; delegate only if a sub-agent is available. Don't ask the user for facts you can reliably find yourself. The _decisions_ are the user's: put each to them and wait.

The session is done when the frontier is empty: every branch of the design tree visited, nothing left silently assumed. Do not act on it until the user confirms you have reached a shared understanding.

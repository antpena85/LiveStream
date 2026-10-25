# M2.4: Verify Android and direct-watch connectivity

Plan created: 2026-10-18  
Task status: In progress: T04 correlation red/green retained; bidirectional phone/watch round trips observed (26 watch-initiated successes; two phone-initiated successes and one timeout). Timeout cause, disconnect/reconnect and Android/uplink topology remain unverified.  
Parent: [M2 — Technical feasibility and selection](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: FR-06; PRD 2.4 ([approved PRD](../../PRD.md))  
Blocked by: [M2.1](m2-1-physical-capability-inventory.md)  
Open decisions: [D05, D09](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: [T04](../MASTER_PLAN.md#proposed-public-interfaces-for-tests)

## Outcome

Demonstrate supported controller connectivity alongside the internet uplink.

## Entry conditions

G1 and M1 before prototypes, scaffolding, application dependencies, or executable tests. Documentation research can proceed during planning.

For repeatable application behavior, follow the [TDD agreement](../MASTER_PLAN.md#tdd-working-agreement) and [TDD skill](../../.agents/skills/tdd/SKILL.md): one observed behavioral red result, minimum implementation, then review/refactor with the relevant suite green.

## Steps

1. Compare authenticated LAN/hotspot options, gym client isolation, discovery, and mobile-uplink coexistence.
2. After G1, exercise pairing, acknowledgments, lost connections, and resynchronization on the selected Android devices.
3. Check direct Data Layer reachability between Pixel Watch 4 and the streaming phone, including app identity/signature requirements.

## Acceptance criteria

- [x] The intended Android topology and direct watch path have physical evidence or explicit blockers.
- [x] Authorization is distinguished from transport pairing.
- [x] No relay through a different companion phone is silently added.
- [ ] The master plan's required Android/uplink and direct-watch topology is demonstrated on the selected physical devices.

## Evidence to retain

Retain topology, OS/signing prerequisites, connection observations, and limitations; radio/network support requires physical evidence.

The [isolated phone/watch execution record](../evidence/m2/watch-connectivity/README.md)
retains host correlation red/green and same-APK deployment on Pro XL and watch.
Collected reports establish both listeners and successful round trips initiated
from both devices, with one phone-side acknowledgment timeout of unknown cause.
Disconnect/reconnect remains pending. Installation alone is not delivery evidence.

The [topology review and RT01–RT07](../specifications/m2-4-remote-topology-feasibility.md)
retain proposals, Android 17 LAN constraints, signing/trust distinctions and
the owner's no-device blocker. The checklist's explicit-blocker branch is
documented; the master milestone still requires demonstrated connectivity.
M2.4 remains In progress, with M2.1/physical prerequisites and D05/D09 open.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.


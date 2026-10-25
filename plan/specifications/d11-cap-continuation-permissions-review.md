# D11 cap-continuation controller permissions

Prepared and answered: 2026-10-21 (America/New_York)  
Status: Owner decision confirmed as PRD Q36; technical mechanisms remain open.  
Decision: [D11.2/D11.4](m1-5-match-correction-specification.md#d11-policy-register)  
Authority: [PRD Q22/Q25–Q29](../../PRD.md#53-confirmed-scope-decisions),
[device control matrix](../../PRD.md#24-device-control-matrix)  
Companion: [M1.5 specification](m1-5-match-correction-specification.md)

## Scope and confirmed constraints

Q22/Q25 already preserve the configured winning margin and require explicit
current-set continuation before ordinary scoring may exceed a cap. At target 25,
margin 2, cap 30, and points 30–29, the match pauses at a cap conflict; it has
no potential winner. Cancellation preserves that conflict. The next set retains
its configured cap and has no inherited continuation approval.

Q26 includes continuation confirmation in latest-edit Undo. Q27 allows watch
Undo only for actions within its permitted match controls and never skips a
restricted latest edit. Q36 confirms that live cap confirmation and Undo of that
latest confirmation require a phone/tablet. This review records that allocation and its direct
Undo consequence; it does not change the approved scoring policy.

## Owner decision recorded on 2026-10-21

The question provided recommended and alternative answers plus a custom-answer
field. The owner selected the recommended answer, now recorded as
[PRD Q36](../../PRD.md#53-confirmed-scope-decisions).

| Choice | Recommended | Alternative | Answer |
| --- | --- | --- | --- |
| Who may confirm current-set continuation? | Streaming phone and authorized second Android phone/tablet. The watch shows the paused conflict and directs the operator to a phone; it cannot Undo this restricted confirmation. This keeps watch controls simple. | All three roles, including the watch, with deliberate confirmation. The watch may also Undo that latest confirmation under the existing Undo rule; setup/history/reset restrictions remain. | Confirmed: recommended answer, Q36. |

## Confirmed behavior and technical handoff

- Show the current set, score, and consequence before a permitted phone/tablet
  confirms continuation. Preserve points, serve, timeouts, configuration, and
  history; authorize the cap exception for that set only. Keep the winning
  margin. Continuation is separate from confirming a set result or selecting
  its first server, and does not start/stop streaming.
- The watch displays the authoritative paused conflict and a direction to use
  a phone. Deny a direct watch continuation request at the streaming phone;
  hiding a button alone is insufficient. Rejection creates no accepted match edit
  and consumes no existing Undo opportunity.
- A permitted phone/tablet can Undo continuation if it is still the latest
  accepted match edit. With no later edit, restore the unapproved cap conflict
  and its rally pause. The watch must use a phone for that Undo and never skip
  the confirmation to undo an older point.
- If a later allowed point/serve/timeout edit is now latest, ordinary Q26/Q27
  Undo applies to that edit. Undo of a later rally may restore a pre-rally score
  while preserving the earlier continuation approval. Repeated Undo cannot then
  reach the older confirmation.
- Keep controller authorization, fresh snapshots, current match/set identity,
  and reviewed-state checks applicable. A stale confirmation or old-match retry
  cannot approve continuation for a different set. Exact identity/revision,
  receipt, expiry, and storage mechanisms remain technical proposals.
- Q29's phone/tablet setup acknowledgement of prior continuation stays separate
  from this live cap-conflict control. Q36 grants no watch starting-value
  or history editing. A durable restored set uses its recorded approval; reset
  under Q34 and a new set clear continuation approval.

Q36 preserves ordinary watch scoring and Undo of an allowed latest edit after
continuation is approved. It keeps the continuation confirmation itself and
Undo of that latest confirmation on phones/tablets. Q35's watch-reset denial
and the existing setup/history restrictions remain in force.

## Written review cases

CP01–CP10 apply confirmed Q36 alongside the established scoring and Undo rules.
Freshness and storage mechanisms remain proposed, as identified in CP08.
These are planning examples, not tests or
evidence of delivered application behavior. Use target 25, margin 2, cap 30.

| ID | Starting state | Action | Expected result under the confirmed policy / noted technical proposal |
| --- | --- | --- | --- |
| CP01 | Current set at 30–29, serve A, paused cap conflict, no continuation approval | Streaming phone reviews and confirms continuation | Keep points/server/timeouts/history unchanged, authorize this set's continuation, and permit ordinary rally input while other guards are satisfied. Keep margin 2 and later-set configuration. |
| CP02 | CP01's starting state; authorized synchronized second Android controller | Review and confirm continuation remotely | Apply the same state change as CP01 and acknowledge the result. An unauthorized or unsynchronized controller cannot bypass existing command-entry rules. |
| CP03 | CP01's starting state; synchronized watch displays the conflict | Request continuation directly from the watch | Show that a phone is required and reject without mutation. Ordinary rally input remains paused and the existing Undo opportunity is preserved. |
| CP04 | Phone continuation confirmation is the latest accepted edit; no later match edit | Request Undo from the watch | Direct the operator to a phone; preserve continuation and the score. Do not skip it to undo an older point. The denied request does not consume the phone's Undo opportunity. |
| CP05 | CP01's starting state with an existing Undo opportunity | Open continuation review on a permitted phone/tablet, then cancel | Preserve 30–29, serve A, paused conflict, no approval, and the prior Undo opportunity. Opening/canceling a review is not an accepted match edit. |
| CP06 | CP04's latest phone confirmation, with no intervening match edit | Undo from a permitted phone/tablet, then request Undo again | First Undo removes approval and restores the paused conflict at 30–29. Q26 consumes that opportunity; a repeat does not restore approval or reach an older edit. |
| CP07 | Continuation was approved; B then won a rally, reaching 30–30 with serve B | Watch undoes that latest rally | Restore 30–29 and recorded serve A, retaining continuation approval. The latest edit was an allowed point; a second Undo cannot reach the earlier confirmation. |
| CP08 | Continuation was reviewed for one set; a correction, next-set transition, or confirmed reset has since changed the relevant state | Submit the obsolete confirmation | Proposed freshness/identity rule rejects it without granting approval to another set or changing the reset match. Refresh and review current state before a new permitted action. |
| CP09 | Current set at 30–30 with durably retained approval; separately, a later set is newly initialized | Restore the ongoing set; inspect the newly initialized set | Restore the current set's recorded approval without another confirmation; create no approval for the later set. A watch may score within its existing controls after fresh synchronization; no new continuation permission is implied. |
| CP10 | Ongoing setup draft at 30–30, without an observed earlier point sequence | Supply Q29's explicit prior-continuation acknowledgement | Use existing phone/tablet setup controls; watch setup remains unavailable. Do not infer a server from this acknowledgement; an unset server still blocks ordinary rally input. |

## Handoff and evidence limits

[M3.4](../subplans/m3-4-set-and-match-confirmation.md) and
[M7.2](../subplans/m7-2-watch-match-controls.md) consume the selected confirmation
and watch permissions. [M3.5](../subplans/m3-5-undo-and-confirmed-reset.md) consumes
the Undo consequences; [M6.2](../subplans/m6-2-ordered-idempotent-commands.md) and
[M3.7](../subplans/m3-7-atomic-persistence-and-restart.md) still need technical
authorization/freshness, atomic state/Undo/receipt, and recovery dispositions.
[V10](../subplans/m8-10-v10-full-controller-matrix.md) later validates allowed
and denied controls against the selected matrix.

Q36 settles the owner permission choice. M1.5 documentation completion, task counts,
and G1/G2 are unchanged. Existing PRD, volleyball-rules, find-skills, and
plan-status guidance cover this continuation. Focused local skill discovery
checked installed guidance for cap/confirmation/Undo/watch handling; no fresh
online discovery, installation, or platform fact check is claimed. No application
code, dependencies, prototype, application tests, or physical/provider actions
were added.

## Pre-answer documentation validation

Validated on 2026-10-21 with `node plan/tools/check-planning-package.cjs`:
**PASS**, exit code 0, no errors. The checker covered 75 Markdown documents,
1,201 local links, 276 heading targets, 53 task/index records, 159 dependency
edges, eight diagram pairs, and nine locked skills.

Separate reconciliation confirmed unique sequential CP01–CP10 and Q1–Q35,
one pending answer, unchanged task counts of 7 complete / 1 in progress /
45 not started, and 13 open aggregate decision records. No not-started acceptance
box was checked. AGENTS.md and skills-lock.json retain their pre-work hashes.

Manual review checked current-set scope, margin preservation, controller denial,
watch Undo without skipping, cancellation, later-point Undo, stale requests,
durable continuation, and the separate setup acknowledgement. These checks
establish documentation consistency, not application or physical-device behavior.

## Post-answer synchronization and validation

The owner selected the recommended answer on 2026-10-21. Q36 now permits
deliberate live continuation confirmation and Undo of its latest confirmation
on the streaming phone and authorized second Android phone/tablet. The watch
shows the conflict and directs both actions to a phone without skipping the
restricted edit. The PRD criteria/control matrix, M1.5, affected task handoffs,
V10 guidance, companion reviews, architecture, master plan, index, and status
dashboard are synchronized. D11 remains open for technical mechanisms and evidence.

The documentation checker passed after synchronization: 75 Markdown documents,
1,208 local links, 279 heading targets, 53 task/index records, 159 dependency
edges, eight diagram pairs, and nine locked skills; exit code 0, no errors.
Separate reconciliation confirmed unique sequential Q1–Q36 and CP01–CP10,
one confirmed answer row, eighteen confirmed D11 owner choices, unchanged task
counts of 7 complete / 1 in progress / 45 not started, and 13 open aggregate
decision records. Not-started acceptance boxes remain unchecked; AGENTS.md and
skills-lock.json retain their pre-work hashes.

These checks establish documentation consistency. No application code, tests,
dependencies, physical-device results, or provider actions were added. G1/G2,
public-boundary confirmation, and the later validation requirements remain intact.

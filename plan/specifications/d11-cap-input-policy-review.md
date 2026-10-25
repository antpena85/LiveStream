# D11 cap-input and restoration policy

Prepared and answered: 2026-10-21 (America/New_York)  
Status: Owner decision confirmed; written cases apply the selected policy.  
Decision: [D11.2](m1-5-match-correction-specification.md#d11-policy-register)  
Authority: [FR-05](../../PRD.md#fr-05-enter-and-correct-match-information),
[PRD Q22/Q25](../../PRD.md#53-confirmed-scope-decisions)  
Companion: [M1.5 specification](m1-5-match-correction-specification.md)

## Scope

Q22 preserves the configured winning margin at a cap, and Q25 requires explicit
current-set “continue win by two” confirmation before normal scoring exceeds the
cap. This review resolves how an operator enters or restores a score when the
app did not observe that earlier point-by-point sequence.

It does not alter the confirmed live flow. At 30–29 with target 25, margin 2,
and cap 30, normal scoring pauses for continuation or correction. The issue here
is whether a setup/restored score such as 30–30 or 31–29 may assert an earlier
continuation without inventing it silently.

## Owner decision recorded on 2026-10-21

The owner confirmed this policy: require cap to be at least the applicable set target. Permit any nonnegative
starting/restored score below the cap subject to normal preset validation.

At the cap without a margin-satisfying winner, accept the first cap-reaching
score only when neither team has exceeded the cap, then enter the same paused
cap-conflict state used in live scoring. With target 25, margin 2, cap 30, that
means 30–29 may be entered as a conflict, while 30–30 is not accepted without
an explicit statement that the current set had already continued win by two.

For a tied-at-cap score or any score above the cap, require the operator to
explicitly mark current-set continuation as already authorized. Store that
authorization with the current set, not later sets. If the operator cannot make
that statement, reject the draft and ask for a corrected score or a lower
baseline. A restored persisted match uses its durable continuation state rather
than asking again.

This preserves the margin and avoids silently treating a high input as evidence
of prior permission, while still supporting mid-match setup after a referee has
already resolved the cap conflict.

## Written review cases

These cases apply the confirmed Q29 policy. They are planning examples, not executable
tests or delivered behavior.

| ID | Starting input | Action | Expected result under the confirmed policy |
| --- | --- | --- | --- |
| K01 | Target 25, margin 2, cap 30; points 29–29 | Enter the ongoing set | Accept as Playing: no cap conflict or continuation flag. |
| K02 | Same preset; points 30–29 | Enter the ongoing set without prior-continuation acknowledgement | Accept as CapConflict, serve unset or explicitly entered; ordinary rally input is paused until current-set continuation or correction. |
| K03 | Same preset; points 30–30 | Enter the ongoing set without prior-continuation acknowledgement | Reject. This score requires a rally after the first cap-reaching conflict, so do not infer authorization. |
| K04 | Same preset; points 30–30 | Mark “current set already continued win by two” and enter | Accept as Playing with current-set continuation retained. It applies only to this set. |
| K05 | Same preset; points 31–29 | Mark prior continuation and enter | Accept as Potential winner for A because the margin is satisfied; ordinary rally input remains paused for set confirmation or correction under Q24. |
| K06 | Same preset; points 31–30 | Mark prior continuation and enter | Accept as Playing with continuation retained; the margin is not satisfied. |
| K07 | Target 25, margin 2, cap 24 | Save the preset or setup | Reject: cap cannot be below the applicable target. |
| K08 | Persisted current set at 30–30 with durable continuation state | Restore after app restart | Restore Playing with continuation retained; do not prompt again or apply it to the next set. |
| K09 | Current set at 30–29 in CapConflict | Correct score to 29–29 | Accept correction, clear the conflict, and resume ordinary rally input. |

## Boundaries retained

- A score that already meets target and margin remains a potential winner and
  needs normal set/match confirmation; the cap is not an additional target.
- The confirmation or acknowledgement belongs to the current set only. The next
  set uses its configured cap and starts without continuation authorization.
- The active server may be explicitly supplied with a mid-match baseline under
  existing controls. This review does not choose the first server for a fresh
  new set or invent a server from score input.
- Phone/tablet setup controls enter starting values. The watch remains view-only
  for setup/history. The later [permissions review](d11-cap-continuation-permissions-review.md)
  records Q36: live continuation confirmation and Undo of that latest confirmation
  also require a permitted phone/tablet; the watch directs both actions to a phone.
- The exact persistence representation, revision checks, receipts, and recovery
  policy remain D04/D11 technical work.

## Handoff

[M3.1](../subplans/m3-1-match-setup-and-presets.md) needs this policy to validate
custom cap configuration and mid-match starting values. [M3.4](../subplans/m3-4-set-and-match-confirmation.md)
uses the same current-set continuation state. Both tasks remain Not started and
require G1/G2 plus public-boundary confirmation before implementation or tests.

Q29 is recorded in the PRD and planning handoffs are synchronized. This review
neither closes D11 nor opens an implementation gate.

## Documentation validation

Validated after recording Q29 with `node plan/tools/check-planning-package.cjs`:
**PASS**, exit code 0, no errors. The checker covered 71 Markdown documents,
1,115 local links, 259 heading targets, 53 task/index records, 159 dependency
edges, eight diagram pairs, and nine locked skills.

Separate reconciliation confirmed unique Q1–Q29 and unique sequential K01–K09.
Task counts remain 7 complete / 1 in progress / 45 not started, with 13 aggregate
decision records still open. These documentation checks do not establish
application behavior, physical-device support, provider integration, or a
received broadcast.

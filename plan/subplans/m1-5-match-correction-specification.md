# M1.5: Specify correction, cap, and undo decisions

Plan created: 2026-10-18  
Task status: Complete: planning specification completed on 2026-10-18; four additional owner choices recorded on 2026-10-20 and eleven further D11 choices on 2026-10-21; remaining D11 technical details explicitly open.  
Parent: [M1 — Planning package](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: FR-05; PRD 2.3 ([approved PRD](../../PRD.md))  
Blocked by: [M0.1](m0-1-approved-requirements-baseline.md)  
Open decisions: [D11](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: [T01](../MASTER_PLAN.md#proposed-public-interfaces-for-tests)

## Outcome

Make unresolved match semantics reviewable before implementing them.

## Entry conditions

Documentation/review only. Owner facts or answers are required where the task calls for them.

## Steps

1. Work through independent examples for point subtraction versus undo, hard-cap ties/margins, and starting values.
2. Specify revision-bound undo, reset/new-match identity, and stale set confirmation as proposals; record the owner's one-step Undo depth and restart decision separately.
3. Work through historical corrections that change winners or make later sets inconsistent; obtain an owner decision before implementation.

## Acceptance criteria

- [x] Worked examples have explicit starting state, command, and expected result.
- [x] Unanswered semantics remain D11 blockers for affected behaviors.
- [x] Final-set points, explicit confirmation, team identity, and customizable allowances follow the PRD.

## Evidence to retain

Retain proposed examples and actual owner answers; the volleyball skill's defaults cannot override PRD exceptions.

The [worked specification](../specifications/m1-5-match-correction-specification.md)
contains 36 original, twelve supplemental, and twelve Undo/watch before/command/after examples, the D11 policy register,
owner answers, and the handoff to scoring, persistence, and remote-command tasks.
The owner confirmed that point removal preserves serve, conflicting historical
corrections are rejected, and one-step Undo survives restart. PRD Q16–Q18 retain
these answers. PRD Q22–Q25 record the 2026-10-20 follow-up: preserve the winning
margin at a cap conflict, allow totals-only starting baselines, and pause
ordinary rally input at a potential winner, and require confirmed current-set
continuation beyond the cap. PRD Q26/Q27 record the 2026-10-21 follow-up:
all listed accepted match edits share consumed one-step Undo, and watch Undo
is limited to its allowed controls without skipping a restricted latest edit.
PRD Q28 also permits completed-match entry only with a complete ordered valid
history. Q29 settles cap-input validation; Q30 settles conditional reopening
after a coherent historical correction. The [baseline/history review](../specifications/d11-baseline-history-policy-review.md)
records Q31/Q32 and B01–B12: complete matching history replaces the baseline
without double counting, and reviewed starting-total corrections remain possible
only when later play stays valid. Technical mechanisms remain open.
The [new-set serve/reset review](../specifications/d11-new-set-serve-and-reset-policy-review.md)
records Q33–Q35 and S01–S14: explicit first-server selection, retained reset
fields, and confirmed reset on phones/tablets only. It does not change this
task's documentation completion.
The [cap-continuation permissions review](../specifications/d11-cap-continuation-permissions-review.md)
records Q36 and CP01–CP10: live continuation confirmation and Undo of its latest
confirmation require a permitted phone/tablet; the watch directs both actions to
a phone and never skips that edit. No application result is recorded.

All three documentation criteria have retained evidence. M1.5 is complete for
planning; D11 stays open for revision/atomic reconciliation mechanisms,
reset identity and protocol-retention
details before their affected implementation.
The request to execute this planning task does not open G1 or establish
application behavior, executable tests, or boundary confirmation.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.


# M1.6: Specify abbreviations, layout, and readability

Plan created: 2026-10-18  
Task status: Complete: written specification and static illustration recorded on 2026-10-18; twelve owner display decisions now recorded through 2026-10-23; remaining D10 evidence explicitly open.  
Parent: [M1 — Planning package](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: FR-04 ([approved PRD](../../PRD.md))  
Blocked by: [M0.1](m0-1-approved-requirements-baseline.md)  
Open decisions: [D10](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: [T05, T06](../MASTER_PLAN.md#proposed-public-interfaces-for-tests)

## Outcome

Define a compact scoreboard specification for review.

## Entry conditions

Documentation/review only. Owner facts or answers are required where the task calls for them.

## Steps

1. Propose abbreviation limits/truncation and required fields: points, compact sets, timeouts remaining, and serve.
2. Describe landscape 16:9 placement, proportional safe margins, contrast, and identity-preserving swaps.
3. Define a readability review at 720p and at the eventual quality floor; retain choices as proposals until decided.

## Acceptance criteria

- [x] Full names remain editable in match information while the overlay uses abbreviations.
- [x] Preview, controller values, and outgoing overlay share committed values.
- [x] No theme expansion, countdown, or unapproved numeric layout limit is introduced.

## Evidence to retain

Retain a written layout specification and review criteria; actual rendered/broadcast readability remains future evidence.

The [worked specification](../specifications/m1-6-overlay-display-specification.md)
records required fields, abbreviation/overflow proposals, shared committed
values, proposed layout geometry, 16 display examples, and a future readability
review. The owner confirmed configurable placement presets (four corners and
bottom-center), editable 1–5-character codes with validation, and numeric `T/O`
counts. PRD Q19–Q21 retain those answers. The follow-up
[placement/control review](../specifications/d10-placement-control-policy-review.md)
records three additional answers as Q37–Q39 and ten written PL01–PL10 cases:
top-left without a saved preference, per-streaming-phone placement retention
across matches/restarts/reset, and phone/tablet changes before/during a stream
with no watch placement control. Q40 and PU01–PU08 then exclude placement from
match Undo: preserve its opportunity and keep the current position during Undo.
Application behavior remains unimplemented.
The [abbreviation-validation review](../specifications/d10-abbreviation-validation-policy-review.md)
records Q41/Q42 and eleven AV01–AV11 cases: uppercase A–Z/digit entry after
reviewed trimming/uppercase conversion, and matching codes permitted after a
warning and explicit confirmation. Stable team identities stay separate;
remaining recovery/technical/readability work does not change this task's
documentation completion.
Q43 and RC01–RC08 preserve nonconforming saved codes and match data, keep
scoring/corrections available, and require phone/tablet correction before
broadcast start. This settles recovery policy while leaving its mechanisms open.

The [numeric-fit review](../specifications/d10-numeric-fit-and-overflow-policy-review.md)
records Q44/Q45 and N01–N14 on 2026-10-23: widen/rearrange the block within safe
bounds; if numbers still cannot fit legibly, stop broadcasting with notice,
preserve the full new match value, and keep scoring available off-air. This
settles the owner failure policy, leaving numeric capacity, exact layouts,
durable shutdown/recovery, and encoded/received readability open.

The linked editable SVG and inspected PNG are static documentation artwork.
All three documentation criteria have retained evidence; actual rendered-app,
encoded, and received-broadcast readability remain untested. Technical preference
contracts, exact geometry/type/contrast, numeric capacity and stop mechanisms,
and quality-floor evidence remain D10 work. M1.6 completes M1's documentation
tasks; the request to execute it does not open G1 or confirm T05/T06 boundaries.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.


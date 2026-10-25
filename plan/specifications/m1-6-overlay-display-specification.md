# M1.6 broadcast scoreboard specification

Recorded: 2026-10-18 (America/New_York)  
Status: Planning specification; twelve owner display decisions recorded, remaining D10 technical details and physical readability open.  
Task: [M1.6](../subplans/m1-6-overlay-display-specification.md)  
Authority: [PRD FR-04](../../PRD.md#fr-04-display-an-indoor-volleyball-broadcast-scoreboard), [control matrix](../../PRD.md#24-device-control-matrix)  
Decision: [D10](../MASTER_PLAN.md#7-open-decision-register)

## Requirements and scope

The broadcast contains both teams' abbreviations, current set points, a compact
sets-won pair, each team's timeouts remaining, and an active serving indicator.
Output is landscape 16:9, with the scoreboard encoded into the outgoing video.
The local preview and controller displays use the same accepted match values.
Full team names remain editable in match information; the watch stays view-only
for names/abbreviations and historical values under the approved control matrix.

The [volleyball-rules skill](../../.agents/skills/volleyball-rules/SKILL.md)
supports the layout discussion. Its example character lengths, safe margin,
colors, and typography are guidance, not previously approved numeric limits.
The PRD overrides generic examples: use customized timeout allowances, preserve
final-set points, and keep display swaps deliberate and identity-preserving.

This is a written planning specification and static illustration, without an
application UI, compositor, prototype, test harness, or selected toolkit. A
successful illustration render cannot establish readability in encoded or
received video. No themes, countdown timer, logos, sponsor panels, scrolling
text, or arbitrary drag/resize editor are added. The owner-requested preset
placement control is included below.

## Owner decisions recorded on 2026-10-18

The owner answered the M1.6 questions and a placement follow-up:

1. **Configurable placement using presets:** choose a corner or bottom-center.
   The preset set is top-left, top-right, bottom-left, bottom-right, bottom-center.
2. **Editable abbreviations of 1–5 characters:** validate entry instead of
   silently truncating it. Full team names remain separate.
3. **Numeric remaining timeouts labeled `T/O`:** use the same representation for
   default and customized allowances.

[PRD Q19–Q21](../../PRD.md#53-confirmed-scope-decisions) retain these answers.
Those original answers did not approve exact geometry, styling, an initial
default position, or remote/live placement-control permissions. The owner later
confirmed Q37–Q39 on 2026-10-21 in the
[placement/control review](d10-placement-control-policy-review.md): top-left
without a saved preference; placement retained per streaming phone across
matches, restarts, and confirmed reset; and phone/tablet-only changes before
or during a live stream. Q40 subsequently keeps placement outside match Undo:
preserve the existing match opportunity, and keep the current position when Undo
reverses the latest match edit. Change placement back with the permitted phone/tablet control.

## D10 display choices

Confirmed portions are distinguished from unresolved choices. Questions provided
clear alternatives and a custom-answer field, which the owner used to request
configurable placement. The follow-up established preset positions. Other
proposals remain **Open** until their required decisions/evidence are recorded.

| ID | Recommended proposal | Alternative / remaining evidence |
| --- | --- | --- |
| D10.1 — Placement | Confirmed Q19/Q37–Q40: four corners and bottom-center; top-left without a saved preference; save per streaming phone across matches/restarts/reset; allow phone/tablet changes before/during a stream and deny watch changes. Placement is outside match Undo, preserving its current opportunity and the selected position when Undo is used. Propose the same block at each anchor. | Technical storage/revision contracts remain open. Free drag/resize is not selected. |
| D10.2 — Abbreviations | Confirmed Q20/Q41–Q43: editable 1–5 uppercase A–Z/digit codes with reviewed trimming/uppercase conversion; reject unsupported input without truncation. Matching codes are allowed after warning/confirmation with separate stable identities. Preserve nonconforming saved codes and match data for phone/tablet correction before broadcast start; keep scoring/corrections available. | Technical recovery/reviewed-state contracts and actual rendering/fit still need disposition/evidence. |
| D10.3 — Timeouts | Confirmed: number per team labeled `T/O`, showing remaining timeouts for default and custom allowances. | Typeface, field dimensions, and rendered numeric fit remain in D10.5/D10.7. No dot fallback or timer is selected. |
| D10.4 — Geometry | Begin with an inset of 5% of frame width on the horizontal axis and 5% of height on the vertical axis. Place the complete block inside that rectangle. | This is a candidate margin, not a broadcast-standard claim or approved constraint. Final bounds, maximum coverage, and player-control collisions need output review. |
| D10.5 — Type and contrast | Bold plain type, largest numerals for points, a solid dark plate with light text, and a serve marker identifiable by shape/position. Evaluate a text contrast target of at least 4.5:1. | Exact fonts, sizes, colors, marker shape, and contrast target require review. Measure the actual composited result and inspect compression; no color-only meaning. |
| D10.6 — Quality floor | Keep required fields intact and review at the lowest output size/bitrate selected by D07 as well as 720p. | D07 quality bounds and real encoder/provider results are missing. No minimum supported resolution, font size, or bitrate is established here. |
| D10.7 — Overflow and edge cases | Q41–Q43 settle code entry/duplicates/recovery. Q44 widens/rearranges the complete block within the safe area with full numbers and reviewed readable type. Q45 stops publishing with notice if a valid numeric value still cannot fit; retain the full new match and off-air scoring. Block start while fit remains unresolved. | Numeric ranges, exact layouts, durable acceptance/shutdown/recovery contracts, and rendered evidence remain open. No new sporting cap or rejection of a valid score to preserve publishing is selected. |

D10 remains open even after a planning task or an owner layout choice is
complete: numerical geometry and actual readability still need evidence.
The focused [abbreviation-validation review](d10-abbreviation-validation-policy-review.md)
records confirmed Q41/Q42 and AV01–AV11. The owner selected uppercase A–Z/digit
validation and the alternative allowing duplicate codes after a warning and
explicit confirmation. International-character entry and strict uniqueness were
not selected; technical contracts and physical readability remain open.
Its Q43 section and RC01–RC08 additionally settle saved-code recovery:
preserve the original codes and match for correction, keep scoring usable,
and require phone/tablet correction before any broadcast-start control succeeds.

## Team text and numerical values

D10.2's confirmed entry rule is 1–5 characters with validation rather than silent
truncation. The following details separate that rule from proposed mechanics:

1. Keep the editable full name and abbreviation as separate match fields. Do
   not overwrite a name when its abbreviation changes or derive new codes from
   name edits silently. An authorized Android controller can edit these fields;
   the watch can display them under the PRD matrix.
2. Under Q41, trim outside spaces and convert lowercase a–z to A–Z in the reviewed
   candidate before saving. Accept 1–5 uppercase A–Z/digit characters, counting
   the final letters/digits. Reject internal spaces, punctuation, emoji, and
   other characters rather than transliterating or expanding them into a valid
   code. Keep the existing committed code during editing/review.
3. Reject blank/over-limit entry with a field-specific result while keeping the
   previous committed code. Do not silently slice a code, scroll it, or replace
   it with a new team label.
4. Under Q43, restore nonconforming saved codes and the match unchanged, retain
   permitted scoring/corrections, and require phone/tablet review and correction
   before broadcast start. Never silently shorten or replace the original codes.
   Apply the same readiness restriction to local/Android/watch start requests;
   schema detection and recovery mechanisms remain technical work. No import
   feature or automatic publishing resume is selected.
5. Under Q42, compare prepared codes without regard to case. Allow matching
   codes only after a warning and explicit confirmation during the changed-code
   edit. Canceling preserves the previous codes and Undo opportunity. Never
   merge teams; accepted code changes remain Q26 match edits, with phone-only
   Undo under Q27 and current placement preserved under Q40.
6. Render points, sets, and remaining timeouts as their complete decimal values.
   A narrow cell must not turn `100` into `10`, clip a minus sign into apparent
   validity, or replace a value with `99+`. Reject invalid match counts through
   match validation, not by altering the displayed truth.

Custom matches may have more than two allowed timeouts, and no-cap scoring has
no approved two-digit ceiling. A static example at 25 points and two timeouts is
not a numeric product limit. Determine practical numeric ranges and fit before
claiming a final renderer is ready. Similar-looking or duplicate abbreviations
must not merge team identities; Q42 settles warning/confirmation for matching
codes. This does not add fuzzy lookalike detection or a new on-air label.

The [2026-10-23 numeric-fit review](d10-numeric-fit-and-overflow-policy-review.md)
records confirmed Q44/Q45 and fourteen written cases, N01–N14. Widen/rearrange
the complete block within safe bounds, keeping full numbers and reviewed
readable type. If a valid numeric value still cannot fit, stop publishing with
notice, retain the full new match, and keep scoring available off-air. Do not
reject the valid score to preserve publishing. Timeout replenishment, custom
sets, Undo, and remote retries follow that policy. Numeric capacity, exact
layout, durable stop sequencing, and the D07 readable output envelope remain
open; no numeric ceiling, font size, output mode, or general stop policy is selected.

## Shared committed values

The proposed display projection takes one accepted match snapshot containing
stable team IDs, abbreviations, points, sets won, timeouts remaining, serving
team, and display order, with its match identity/revision. This describes the
contract's meaning, not a chosen application API or storage layout.

- Preview, outgoing composition, and controller displays derive values from
  the same committed state. They may use different layouts; a phone/watch UI
  need not reproduce the broadcast artwork.
- Accepted values become eligible for rendering together, without a frame that
  pairs a new score with an old server or swapped values from different teams.
  Pending/rejected edits do not change the committed broadcast values.
- A display swap moves the whole team group: abbreviation, points, its side of
  the compact sets pair, remaining timeouts, and serving marker. Names, history,
  stable IDs, and serve ownership do not change. Swapping twice restores order.
- M1.5's confirmed point-removal policy preserves serve; Undo restores the
  recorded prior state. The overlay follows that resulting state without
  independently recalculating scoring or historical corrections.
- If serving possession is genuinely unset, show no active team marker rather
  than inventing a server. Q33 requires explicit selection for a new set. Whenever
  serve is known, mark the committed serving team, including after a swap.
- Controller disconnect and stream interruption preserve match values. A
  controller shows stale/disconnected status in its own UI; the broadcast does
  not need a new connection-status panel. Reconnect refreshes from the phone.
- Viewers receive the scoreboard in encoded frames. A controller acknowledgment
  does not establish the arrival time of a particular received video frame;
  viewer playback delay remains separate from the PRD's remote-response target.

## Layout candidate and static illustration

The owner selected configurable placement presets. The layout candidate uses
one compact block with two team groups, placed at the selected anchor.
Each group holds its code, prominent points, and labeled remaining timeouts.
A small central `SETS` label above a pair such as `2 - 1` follows the left/right
team order. A fixed slot next to each code holds the serve marker; only the
serving team's slot is active. Do not shift text when the marker changes teams.

Illustrative values are Eagles (`EAG`), Panthers (`PAN`), points 24–22, sets
2–1, remaining timeouts 1–2, serve Eagles. The layout uses two team groups inside
one background plate; it is not a new theme system or a custom-position editor.

For an output frame of width `W` and height `H`, candidate horizontal/vertical
insets `mx`/`my`, and a fitted scoreboard block of width `B` and height `b`,
the preset anchors are:

| Confirmed preset | Proposed block origin `(x, y)` |
| --- | --- |
| Top-left | `(mx, my)` |
| Top-right | `(W - mx - B, my)` |
| Bottom-left | `(mx, H - my - b)` |
| Bottom-right | `(W - mx - B, H - my - b)` |
| Bottom-center | `((W - B) / 2, H - my - b)` |

Anchor changes do not change team order, values, or serving ownership. Every
placement uses the same readable field layout and fits entirely within the
eventually approved safe rectangle. Inset/block size and rounding are technical
proposals. Q37 selects top-left when no preference is saved; Q38 retains each
streaming phone's selected preset across matches, restarts, and confirmed reset.
Q39 allows the streaming phone and authorized second Android phone/tablet to
change placement before or during a live stream, and denies watch changes.
The focused [placement/control review](d10-placement-control-policy-review.md)
records these answers and PL01–PL10, followed by Q40 and PU01–PU08. A placement
change preserves the match Undo opportunity; Undo of the latest match edit keeps
the current position. Technical storage/revision mechanisms are unselected.

[Editable SVG illustration](../../docs/diagrams/source/scoreboard-layout.svg)
and [PNG preview](../../docs/diagrams/rendered/scoreboard-layout.png) show the
candidate at top-left and bottom-center in two schematic 1280×720 frames. The
court, example scores, safe-area guides, and explanatory labels are documentation
artwork. Only the
scoreboard plate represents candidate broadcast content; guides and annotations
are not proposed on-air graphics. The schematic court is not a hardware capture.

For the proposed 5% inset, the arithmetic is:

| Example frame | Horizontal inset (`0.05 × width`) | Vertical inset (`0.05 × height`) |
| --- | --- | --- |
| 1280×720 | 64 pixels per side | 36 pixels per edge |
| 1920×1080 | 96 pixels per side | 54 pixels per edge |

The second row is a scaling example, not a selected output mode. All point
sizes and block dimensions in the illustration are candidate artwork dimensions,
not approved numeric layout limits. The inset applies separately in both axes;
using 36 pixels horizontally at 720p would not be a 5%-of-width inset.

The future compositor positions the overlay in final video coordinates after
camera crop/scale/orientation. Preview mirroring or a camera switch must not
mirror the text, swap identities, or cause the scoreboard to leave its output
bounds. Preserve landscape 16:9 through actual supported quality changes.
Provider player buttons, captions, crops, and viewing modes need received-video
review; the candidate inset is not a promise to avoid every player control.

## Readability review and retained evidence

These are proposed review criteria to make D10 measurable. They require owner
review before being treated as settled limits. The PRD's 720p readability and
20-change received-overlay target remain the approved baseline.

W3C's [contrast guidance](https://www.w3.org/WAI/WCAG22/Understanding/contrast-minimum.html)
describes 4.5:1 for ordinary text and images of text, while its
[use-of-color guidance](https://www.w3.org/WAI/WCAG22/Understanding/use-of-color.html)
describes conveying information through cues beyond color. These inform the
proposals; they do not certify the application, set video font sizes, or prove
readability after compression. References checked on 2026-10-18.

| Check | Proposed method / pass observation | Evidence still required |
| --- | --- | --- |
| Required fields | At native output size, identify both abbreviations, both point values, the corresponding sets pair, both timeout counts, and the serving team without zoom. No overlap, cropping, ellipsis, or ambiguous ownership. | Actual rendered frames, chosen sizes/font, and owner readability review. |
| Backgrounds and contrast | Inspect over a bright floor, dark wall, court lines, uniforms, and camera movement. The plate preserves text contrast; marker shape/position remains understandable in grayscale. If transparency is later proposed, evaluate after compositing over every background case. | Representative court imagery and measured rendered colors/contrast. A solid plate is the initial candidate. |
| Character/numeric fit | Review short and longest allowed codes, wide glyphs, unequal code lengths, multi-digit scores including 99→100, and zero/custom timeout allowances. Never shrink below the eventual accepted text size just to hide a fit failure. | Decided glyph/range/overflow rules and rendered output for those cases. |
| Team ownership | Compare asymmetric values before/after swaps, sideouts, point removal, Undo, and accepted historical corrections. A set/match confirmation preserves the resulting correct values, including final points. | Shared-projection and encoded-frame checks through future T05/T06 work. |
| Baseline | Review actual encoded 1280×720 landscape output and destination playback at native size and normal phone playback size, recording the viewing device/mode. Distinguish unreadable content from content obscured by temporary player controls. | M4.3 encoded output and V02 received-broadcast observation. |
| Adaptive floor | Repeat at every selected output size including D07's minimum, using the decided low-bitrate configuration and a quality transition. Required fields and identity remain intact. | Selected D07 parameters and actual media/provider evidence. M1.6 does not choose a floor. |
| Preview/controller agreement | Compare the same committed revision after updates have propagated; controllers show equivalent values even when their layout differs. Separate transport/overlay response from viewer playback delay. | Behavioral evidence plus physical Android/watch and broadcast checks. |

Record the match configuration, team codes/values, revision or script step,
frame size, encoder bitrate/configuration, destination, observation timestamp,
viewing device/player mode, background case, expected/observed result, and any
clipped or ambiguous field. Identify static illustrations, local renders,
encoded output, and received frames separately. An unavailable run stays pending.

## Worked display examples

These are expected outcomes for planning, not tests that have run. Examples
involving unresolved D10 rules are explicitly conditional proposals.

| ID / basis | Starting values | Action / review | Expected display |
| --- | --- | --- | --- |
| O01 — PRD fields | EAG left/PAN right; points 24–22, sets 2–1, timeouts 1–2, serve EAG | Project the accepted snapshot | EAG 24, PAN 22, `SETS 2 - 1`, EAG's remaining count 1 and PAN's 2, serve marker with EAG. |
| O02 — PRD side swap | O01 | Accept a display swap | PAN 22 left/EAG 24 right, `SETS 1 - 2`, timeouts PAN 2/EAG 1, serve marker moves with EAG to the right. |
| O03 — PRD sideout | O01, serve EAG | Award a PAN rally | Points 24–23; serve marker moves to PAN. Sets and timeout counts are unchanged. |
| O04 — PRD Q16 | O03 | Remove one PAN point | Points 24–22; serve remains PAN. The overlay must not infer EAG as server. |
| O05 — PRD Q18; proposed Undo mechanics | O03 was the latest match edit, before O04 is applied | Undo that sideout | Restore points 24–22 and serve EAG, with the earlier timeouts/sets retained. |
| O06 — Confirmed D10.2 | Full name Eagles, committed code EAG | Enter a six-character code under the maximum of five | Reject the entry; keep EAG on air and keep the full name editable. No silent `EAGLE…` substitution. |
| O07 — PRD full-name separation | Full name Eagles, code EAG | Rename the full name to North Eagles without changing the code | Match information shows North Eagles; broadcast still uses EAG. Watch name editing remains unavailable. |
| O08 — Confirmed D10.3 | Custom timeout allowance 3, remaining 3–0 | Project remaining timeouts | `T/O 3` and `T/O 0`; do not draw a fixed two-dot interpretation or a countdown. |
| O09 — PRD nonfinal confirmation / Q33 | Best-of-5 set 1 at 25–23, sets 0–0, custom allowance 3, timeouts 1–0 | Confirm set 1 for EAG | Sets 1–0, new set points 0–0, timeouts 3–3. No serving marker until explicit first-server selection; ordinary rally scoring stays blocked until then. |
| O10 — PRD final retention | Best-of-3 deciding set at 16–14, sets 1–1, timeouts 1–0 | Confirm final set for EAG | Sets 2–1, final points 16–14 and timeouts 1–0 remain visible; no empty fourth set. |
| O11 — PRD Q17 | EAG won sets 1/2, PAN won set 3; active set 4 points 8–6, sets 2–1 | Correct set 3 to an EAG win, which would finish the match before set 4 | Reject the whole correction; points remain 8–6 and sets 2–1. No transient recomputed total appears as accepted. |
| O12 — PRD landscape / quality continuity | O01 at 1280×720 | Switch to a lower supported output configuration decided by D07 | Preserve every value, team order, serve, and 16:9 orientation; repeat readability checks at that actual configuration. This does not approve a resolution or bitrate. |
| O13 — Confirmed Q44/Q45; fit evidence pending | A no-cap configuration permits points 99–98 | Accept an EAG point to 100–98 | Show complete `100` using the reviewed wider/rearranged layout. If a diagnosed numeric fit failure remains, stop broadcasting with notice and preserve full 100–98 plus off-air scoring. No wrap, clipping, `99+`, or rejected valid point to preserve publishing. Actual fit/readability remains untested. |
| O14 — PRD accepted values | O01 committed; another edit is pending or rejected | Render while awaiting its result | Keep O01's complete committed values. Switch to a coherent new snapshot only after acceptance. |
| O15 — PRD full name / proposed code policy | O01 committed; a disconnected controller has stale values | Reconnect the controller | Refresh EAG/PAN and O01's points/sets/timeouts/serve from the phone; do not replace them with locally derived or stale text. |
| O16 — Confirmed D10.1 / proposed anchors | O01 at top-left | Configure bottom-right before or during streaming from the streaming phone or authorized Android controller | The same complete scoreboard moves to bottom-right within the safe rectangle. EAG stays left within the block, points/sets/timeouts remain unchanged, and EAG retains serve. Q39 denies a watch placement request. Exact anchor geometry remains proposed. |

## Handoff and planning acceptance

| Consumer | Material supplied / remaining condition |
| --- | --- |
| [M3.9](../subplans/m3-9-local-controls-and-scoreboard-projection.md) | O01–O16, PL01–PL10, PU01–PU08, AV01–AV11, RC01–RC08, N01–N14, and field ownership; apply confirmed Q37–Q45, settle remaining D10 technical/visual rules, and preserve equivalent committed preview/controller values. |
| [M4.3](../subplans/m4-3-encoded-scoreboard-composition.md) | Output-coordinate layout, all required fields, encoded-frame readability, mirroring and quality-change review. |
| [M2.6](../subplans/m2-6-adaptive-quality-policy.md) | D10.6/D10.7 handoff: choose quality limits with readable overlay evidence; a layout illustration does not choose those limits. |
| [V02](../subplans/m8-2-v02-received-overlay.md) | The PRD's 20 received-overlay changes plus applicable layout/ownership/readability cases. This document does not replace or claim completion of that script. |

M1.6's documentation acceptance is addressed by separate full-name/code fields,
the shared committed-value contract, and a scoped layout/readability specification.
All unapproved numeric examples are labeled proposals; physical/encoded/received
readability remains future evidence. D10 and the application entry gates stay open.
Future T05/T06 boundaries need confirmation before executable tests; behavior
then follows the existing TDD agreement after the relevant implementation gates.

## Documentation validation

Validated on 2026-10-18 with `node plan/tools/check-planning-package.cjs`:
**PASS**, exit code 0, no errors. The checker covered 67 Markdown documents,
1,050 local links, 236 heading targets, 53 task files/index rows, 159 dependency
edges, eight existing Mermaid pairs, and nine locked skills. The standalone
SVG layout illustration is separate from those eight Mermaid pairs.

A separate documentation reconciliation passed for 16 unique O01–O16 examples,
their starting/action/expected fields, all seven completed acceptance lists,
all nine milestone rows, the 7/53 total (46 not started), M1 at 6/6, 13 open
aggregate decisions, and PRD Q19–Q21. Candidate 5% inset arithmetic and all five
anchor positions fit the candidate safe rectangle at the 720p reference size
and the illustrative 1920×1080 scale. These are geometry checks, not selected
numeric product limits or evidence for an additional supported output mode.

The editable SVG parsed as XML and rendered to a 1280×1680 PNG using existing
skill tooling. The PNG was visually inspected: reference abbreviations, points,
sets, `T/O` counts, serve marker, safe-area guides, and explanatory labels are
present without clipping in both placement examples. This checks the artwork's
three-character reference codes; longest/wide glyphs, large numeric values,
all placement variants in a real compositor, and actual video readability
remain future evidence under D10/M4.3/V02.

Manual review checked team ownership after swaps, point removal versus Undo,
custom timeout allowances, final-point retention, configurable placement versus
team-order swaps, and separation of approved choices from remaining proposals.
No application behavior, executable application test, physical-device, encoded
frame, or received-broadcast result is claimed.

# D10 scoreboard placement and control policy

Prepared and answered: 2026-10-21 (America/New_York)  
Status: Four owner decisions confirmed as PRD Q37–Q40; remaining D10 details open.  
Decision: [D10](../MASTER_PLAN.md#7-open-decision-register)  
Authority: [PRD Q19](../../PRD.md#53-confirmed-scope-decisions),
[device control matrix](../../PRD.md#24-device-control-matrix)  
Companion: [M1.6 scoreboard specification](m1-6-overlay-display-specification.md)

## Scope and fixed constraints

The owner has already selected five configurable on-air placement presets:
top-left, top-right, bottom-left, bottom-right, and bottom-center. A placement
moves one complete scoreboard block. It never changes team identity or display
order, points, sets, timeouts, serve possession, match history, or stream state.
The configurable presets are distinct from a team display swap, which changes
the left/right presentation order while preserving stable team identities.

This review records the owner's policy for three related concerns:
the initial preset, retention of a chosen preset, and who may make a change.
It does not select safe-area measurements, block size, typography, contrast,
glyph handling, numeric overflow behavior, a storage technology, or evidence
of encoded/received-video legibility. The follow-up Q40 explicitly excludes
placement from Q26 match Undo, preserving both its existing opportunity and
the selected position when Undo is used.

## Owner decisions recorded on 2026-10-21

The owner selected all three recommended answers in the question picker,
which also offered alternatives and a custom-answer field. The selections are
recorded as [PRD Q37–Q39](../../PRD.md#53-confirmed-scope-decisions).

| ID | Question | Recommended answer | Alternatives | Status |
| --- | --- | --- | --- | --- |
| P01 / Q37 | Which placement is used when a streaming phone has no saved preference? | Top-left. It is a familiar broadcast location and leaves bottom-center more available for action. | Bottom-center; top-right; bottom-left; bottom-right; or custom answer. | Confirmed: recommended, Q37. |
| P02 / Q38 | How does a selected placement persist? | Save it as a broadcast preference on the active streaming phone, applying it to future matches and restarts; a confirmed match reset does not change it. Each streaming phone retains its own preference. | Save it only with the current match; ask for a placement each match; or custom answer. | Confirmed: recommended, Q38. |
| P03 / Q39 | Who may change placement before or during a live stream? | Streaming phone and authorized second Android phone/tablet. The watch cannot change it. | Streaming phone only before/during a stream; include the watch; streaming phone only before going live; or custom answer. | Confirmed: recommended, Q39. |

Under Q38, the preference belongs to the streaming phone that holds it.
An authorized controller changes the streaming phone's active preference after
the normal authorization and freshness checks. A different phone becoming the
streamer follows its own saved preference until a later product decision states
otherwise. Phone-specific retention is confirmed; the storage and synchronization
mechanisms remain technical work.

## Confirmed behavior and technical handoff

- With no saved preference, a newly configured broadcast starts with top-left.
  Otherwise it uses that streaming phone's saved preset. The operator may
  choose any of the other four approved presets before or during streaming.
- An accepted preset change repositions the entire committed-value projection
  together. It does not score a rally, select a server, change timeouts, alter
  display order, interrupt media, or rewrite history.
- A selected preset survives app restart and setup of a later match on that
  streaming phone. Q38 also requires the Q34 confirmed match reset to preserve
  placement; Q34 alone had not settled placement retention.
- The streaming phone accepts a remote placement request only from an authorized,
  synchronized Android phone/tablet. The watch exposes no placement command;
  this grants no additional watch status field or broadcast-preview feature.
  Reject a direct watch placement request at the streaming phone as well as
  omitting that control from the watch UI. Rejection leaves placement and match
  state unchanged. Q40 also preserves the match Undo opportunity.
- A live placement change becomes eligible for preview and outgoing rendering at
  a coherent accepted revision. Viewer playback delay remains a separate media
  concern. The eventual control design needs acknowledgement and failure states
  but no storage or command protocol is selected here.

## Written review cases

PL01–PL10 apply Q37–Q40. Technical revision/storage details remain open as noted.
These are written examples, not
application tests and establish no physical-device, encoded-frame, or
received-broadcast result.

| ID | Starting state | Action | Expected outcome under confirmed Q37–Q39 / noted open detail |
| --- | --- | --- | --- |
| PL01 | Freshly configured stream on a phone with no saved placement preference | Open the broadcast preview | Use top-left as the initial preset; all scoreboard values remain whatever the accepted match snapshot supplies. |
| PL02 | Existing match values with EAG left and PAN right | Select bottom-right before streaming | Move the whole scoreboard block to bottom-right. EAG/PAN order, points, sets, `T/O`, and serve marker stay unchanged. |
| PL03 | Streaming phone saved bottom-center from an earlier match | Confirm the Q34 reset, then set up the next match | Keep bottom-center selected. Reset clears match progress only; it does not change the phone's broadcast preference. |
| PL04 | Streaming phone saved top-right | Restart the app and restore its state | Restore top-right after normal state recovery. The result does not establish the eventual persistence mechanism. |
| PL05 | Authorized synchronized Android phone/tablet; stream is live at top-left | Select bottom-left remotely | Accept the choice and make bottom-left the streaming phone's active preference. Score, server, timeouts, display order, and stream lifecycle do not change. |
| PL06 | Same as PL05, but controller is revoked, stale, or unauthorized | Request bottom-left | Reject and refresh/indicate the failure without changing the active placement. |
| PL07 | Synchronized watch while the stream is live | Attempt to change placement | Provide no placement control and reject a direct watch request at the streaming phone, leaving the active preset and match state untouched. No additional watch status field is required by Q39. |
| PL08 | Streaming phone A saved top-left; different phone B becomes the streaming phone with a saved bottom-center preference | Start a new broadcast on B | Use B's saved bottom-center preference. No unapproved cross-device preference sync is inferred. |
| PL09 | Live scoreboard at bottom-right while a point is pending or rejected | Render during the pending/rejected operation | Keep the last complete accepted values and bottom-right anchor. Do not move or partially update the block from a pending/rejected match edit. |
| PL10 | Live scoreboard changes from top-left to bottom-center | Inspect the post-change snapshot | The new anchor applies to the complete block, without a team display swap or a new serving indicator. Under Q40, preserve the match Undo opportunity; using it keeps bottom-center selected. |

All three recommended choices are confirmed. A saved preset takes precedence
over Q37's initial default. An Android controller edits the active streaming
phone's preference; it does not overwrite its own saved streamer preference.
No cross-phone preference transfer is selected when roles change. The exact
revision, rendering, and storage contracts remain unselected technical mechanisms.

## Placement and match Undo: confirmed Q40

The owner selected the recommended answer to the follow-up question on
2026-10-21. The picker included the alternative and a custom-answer field.

| Question | Recommended answer | Alternative | Owner answer |
| --- | --- | --- | --- |
| A point is scored, then the scoreboard is moved. What does match Undo do? | Keep placement outside match Undo. Preserve the existing opportunity, undo the latest match edit, and keep the chosen position. Change placement back manually on a permitted phone/tablet. | Include placement in one-step match Undo, replacing Undo of the earlier point. Only a permitted phone/tablet may undo the latest placement edit; the watch directs the operator to a phone. | Confirmed: recommended, PRD Q40. |

Placement neither replaces nor consumes the latest match Undo opportunity, and
does not recreate one after it has been consumed or cleared. The existing
Q18/Q26/Q27 rules still govern durability, one-step consumption, and authority.
An intervening placement change does not make a restricted names/history/cap
confirmation edit eligible for watch Undo. It also does not block watch Undo of
an otherwise permitted latest match edit. No older match edit is skipped.

Undo restores the affected recorded match values while retaining the streaming
phone's current placement preference. A whole-snapshot restoration must not
silently roll placement back. An operator changes placement back using the
existing Q39 phone/tablet control, not match Undo. No separate placement Undo
feature is selected. Q34 reset still clears match Undo while Q38 retains placement.

PU01–PU08 are written outcomes under confirmed Q40, not executed tests.

| ID | Starting state / sequence | Action | Expected outcome |
| --- | --- | --- | --- |
| PU01 | Points 10–9, serve A; B wins a rally, making 10–10/serve B; then placement changes from top-left to bottom-center | Phone undoes the latest match edit | Restore 10–9 and serve A; retain bottom-center. Consume the one match Undo opportunity. |
| PU02 | PU01's sequence, with placement changed by an authorized Android controller | Watch undoes the latest point | Restore the same recorded score/server and retain bottom-center. The intervening phone-only placement change does not block this permitted point Undo or grant watch placement control. |
| PU03 | A phone changed team A's name after the previous point; placement then changed | Watch requests match Undo | Require a phone for Undo of the name edit; keep name, score, and current placement. Never skip the name edit to undo the older point. |
| PU04 | Match Undo has been consumed, or no accepted match edit is available | Change placement on a permitted phone/tablet, then request match Undo | Retain the new position and match state. Placement does not create, rearm, or provide Redo through match Undo. |
| PU05 | A point remains Undo-eligible; placement changed after it and both states were durably saved | Restart, refresh, then Undo the point | Preserve the restored current placement while undoing the point. Q18 retains the match opportunity; exact atomic storage/recovery mechanisms remain technical work. |
| PU06 | A saved placement and an available match Undo exist | Confirm Q34 reset, then request match Undo | Keep placement under Q38; reset clears match Undo. Do not restore prior-match play or the earlier position. |
| PU07 | Latest match edit is a timeout; the chosen placement is bottom-right | Submit a rejected/unauthorized watch placement request, a duplicate request, or a request selecting the same preset | Leave match values and the match Undo opportunity unchanged. Rejection is not a match edit; exact duplicate receipt mechanics remain technical work. |
| PU08 | Point A was followed by a placement change, then timeout B was accepted | Undo the latest match edit | Undo timeout B only, retaining point A and the current placement. Consume Undo; a repeated Undo does not reach point A or the placement change. |

Reviewed-state checks remain applicable. A stale reviewed request may require
refresh under the eventual command contract; that is distinct from deleting an
otherwise available match Undo opportunity. Q40 does not choose shared versus
separate revision counters, storage records, atomic boundaries, or receipt rules.

## Handoff and evidence limits

[M3.9](../subplans/m3-9-local-controls-and-scoreboard-projection.md) consumes
the final placement policy for local/shared projection. [M4.3](../subplans/m4-3-encoded-scoreboard-composition.md)
must still verify output-coordinate composition and readability, and
[V10](../subplans/m8-10-v10-full-controller-matrix.md) must validate the final
allowed and denied controller matrix. The existing static illustration and
review criteria remain the planning basis; they do not establish layout quality
in a rendered application or received broadcast.

These four confirmed choices do not change M1 completion, task counts, G1/G2,
or D10's open status. No application code, dependencies, prototype, tests,
device checks, or provider actions are added.

## Pre-answer documentation validation

On 2026-10-21, the existing planning-package checker passed with no errors:
76 Markdown documents, 1,220 local links, 281 heading targets, 53 task records,
159 dependency edges, eight diagram pairs, and nine locked skills. Separate
review confirmed ten unique PL01–PL10 cases, three pending choices, and
36 confirmed PRD decision rows ending at Q36. AGENTS.md and skills-lock.json
retain their pre-work hashes.

The follow-up questions explicitly cover all five initial presets, phone-specific
retention, and pre-live-only control as an alternative. Saved preference takes
precedence over the initial default in the conditional cases. These are written
consistency checks; the pending recommendations establish no delivered behavior.

## Post-answer synchronization and validation

The owner selected all three recommended answers on 2026-10-21. Q37–Q39 now
confirm the top-left initial default, per-streaming-phone placement retention
across matches/restarts/reset, and placement changes from the streaming phone
and authorized Android phone/tablet before/during a live stream. Watch placement
changes are denied. The PRD acceptance criteria/control matrix, M1.6 specification,
affected task handoffs, reset/Undo companion reviews, architecture, master plan,
task index, and status dashboard are synchronized. Placement/Undo, technical
preference contracts, and the remaining visual/readability decisions stay open.

The planning-package checker passed after synchronization: 76 Markdown documents,
1,235 local links, 282 heading targets, 53 task records, 159 dependency edges,
eight diagram pairs, nine locked skills, exit code 0, and no errors. Separate
reconciliation confirmed unique sequential Q1–Q39, ten unique PL01–PL10 cases,
three confirmed answer rows, and 13 open aggregate decisions. Task counts remain
7 complete / 1 in progress / 45 not started / 0 unverified, with milestone
totals reconciled to 53. No completed task has an unchecked acceptance item;
not-started acceptance lists remain unchecked. AGENTS.md and skills-lock.json
retain their pre-work hashes.

These are documentation results. No application tests, application code,
dependencies, physical-device results, or provider actions were added. G1/G2
and future public-boundary confirmation remain required.

## Q40 synchronization and validation

On 2026-10-21 the owner selected placement outside match Undo. Q40, PU01–PU08,
the PRD acceptance criteria/control notes, M1.5/M1.6 specifications, Undo/watch
coverage, affected task handoffs, architecture, master plan, task index, and
status dashboard are synchronized. Match Undo retains the current placement;
placement changes preserve the existing opportunity without replacing,
consuming, or recreating it. Q27 still controls authority for the latest match
edit, and Q39 still denies watch placement changes. No additional placement
Undo feature or technical revision/storage mechanism is selected.

The existing planning-package checker passed: 76 Markdown documents, 1,236 local
links, 283 heading targets, 53 task records, 159 dependency edges, eight diagram
pairs, nine locked skills, exit code 0, and no errors. Separate reconciliation
confirmed sequential unique Q1–Q40, PL01–PL10, eight unique PU01–PU08 cases,
one confirmed Q40 answer, seven confirmed D10 owner choices, and 13 still-open
aggregate decision records. Task counts remain 7 complete / 1 in progress /
45 not started / 0 unverified; milestone totals reconcile to 53, entry/evidence
sections are present, and completed/not-started checklists retain their proper
checked/unchecked state. AGENTS.md and skills-lock.json hashes are unchanged.

These are documentation consistency results. Application implementation,
executable tests, device/provider checks, and received-broadcast evidence remain
subject to the existing gates. The next recommended owner-policy review is
abbreviation counting/glyphs/casing and duplicate-code handling under D10.

# Product Requirements Document: Volleyball LiveStream

Status: Approved requirements baseline  
Created: 2026-10-17  
Approved by: Project owner on 2026-10-17  
Device-planning clarifications: Project owner on 2026-10-18  
Microphone-availability clarification: Project owner on 2026-10-18  
Network-default clarification: Project owner on 2026-10-18  
Match-correction clarifications: Project owner on 2026-10-18  
Match-policy follow-up decisions: Project owner on 2026-10-20  
Scoreboard-display clarifications: Project owner on 2026-10-18; numeric-fit follow-up on 2026-10-23  
Project type: Personal project  
Primary platform: Android

The capabilities in section 2.2 and decisions in section 5.3 reflect the user's
requirements and clarification answers. The user accepted this PRD on
2026-10-17, including its acceptance criteria, scoring defaults, control matrix,
first-release non-goals, and conditional validation targets. Explicitly open
details, implementation technologies marked TBD, and recommendations awaiting
a decision remain unresolved; see sections 4 and 5.4. Approval establishes the
requirements baseline. Implementation requires a separate user request.

On 2026-10-23 the owner requested execution of all M2 and confirmed T01–T06.
This authorizes M2 feasibility probes/harness preparation; G2 and later delivery
remain pending. The owner has no physical devices available in this session
and directed research/preparation to continue with measurements pending.
Registrations were reported to exist, but their nonsecret configuration was
subsequently reported unknown. The [M2 execution record](plan/reviews/m2-readiness-and-execution.md)
distinguishes completed preparation/local tests from those missing observations.

Update 2026-10-24: the owner reports that devices are available but not connected
to the workstation. Installed builds and physical results remain unverified.
Q48 changes version 1 to built-in audio, deferring external microphone support
and testing until after version 1. Other physical validation remains required.

Later on 2026-10-24, [ADB observations](plan/evidence/m2/phone-capabilities-2026-10-24.md)
verified authorized Pixel 8 Pro and Pixel 10 Pro XL connections, both reporting
Android 17/API 37, and the diagnostic capability APK opened on both. Other
units/builds, watch pairing, actual capture and provider results remain unverified.

## 1. Executive Summary

### Problem Statement

An individual wants to livestream volleyball from a phone while displaying
match information and updating it without repeatedly handling the camera phone.

### Proposed Solution

Build an Android application that captures the phone's camera and microphone,
adds a volleyball scoreboard to the outgoing video, and streams to services
such as YouTube and Twitch, one destination at a time using a stream URL and
key or optional destination sign-in that configures the connection
automatically. Provide controls on the streaming phone, a second Android
phone/tablet, and a Wear OS watch. Concentrate initially on customizable indoor
volleyball scoring presets, deferring beach volleyball to later releases.

### Success Criteria

These are approved validation targets, subject to the selected hardware,
network conditions, and release scope:

| Measure | Target |
| --- | --- |
| Broadcast reliability | Complete a 60-minute smoke test and a 90–120 minute full-match session to YouTube and a separate session to Twitch at 720p/30 fps on the chosen test phone and a stable connection, without an application crash or an unrequested stream stop. |
| Scoreboard correctness | All 20 scripted changes to names, scores, sets, and timeouts appear correctly in the received broadcast for the indoor preset; also verify a customized match format. |
| Remote response | At least 95% of 50 acknowledged remote score changes appear on the streaming phone and its outgoing overlay within 2 seconds on the supported connection. Viewer playback delay is measured separately. |
| Input continuity | Supported lens changes and built-in audio/mute operation preserve match information and produce no crash during a documented device test. External microphone support and connection/disconnection testing follow version 1 under Q48. |
| Destination sign-in | Complete setup and a test broadcast through each supported sign-in integration without manually finding, copying, or entering a stream URL or key; manual setup remains usable without sign-in. |

The 720p/30 fps reliability test is a stable-connection validation baseline.
Streaming quality must adapt automatically to upload conditions. Also validate
automatic quality reduction in controlled restricted-upload tests over Wi-Fi
and mobile data, while checking scoreboard correctness and match-state
continuity. Adaptation limits and response targets will be set during technical
planning.

Q45 adds a controlled-stop exception for a numeric value that cannot fit
legibly after Q44's layout adjustment: notify the operator, stop broadcasting,
preserve the full new match value, and keep scoring available off-air. This is
not permission for unrequested stops in otherwise supported readable
configurations; the original reliability targets remain applicable there.

## 2. User Experience & Functionality

### 2.1 Users and Main Flow

- **Streamer:** the project owner operating an Android phone at a volleyball
  match, potentially mounted on a tripod.
- **Scorekeeper:** the streamer or a helper using the phone, another mobile
  device, or a wearable to update the match.
- **Viewer:** someone watching the broadcast on the chosen streaming platform.

Main flow:

1. Choose the indoor volleyball preset, customize it if needed (set targets,
   timeouts, deciding set), and enter team names and starting match values.
2. Choose a destination and optionally sign in to configure its connection
   automatically, or enter a stream URL and key manually. Select or create
   the broadcast when the destination requires it, then choose the camera/lens
   and available microphone.
3. Preview the camera and broadcast scoreboard in locked landscape orientation (16:9); check audio input.
4. Pair a remote controller if needed.
5. Start streaming and update the match locally or remotely (points, serve, timeouts, sets, and side swaps).
6. Stop streaming and retain the final match state for review.

### 2.2 Functional Requirements

The capability statements and acceptance criteria below form the approved
requirements baseline. Explicitly open details remain unresolved.

#### FR-01: Livestream to an External Platform

**User story:** As a streamer, I want to broadcast volleyball to a streaming
service so others can watch remotely.

**Required capability:** Livestream to YouTube, Twitch, and compatible
services, one destination at a time. Offer optional sign-in for supported
destinations so the user does not have to obtain or enter the stream URL and
key. Keep manual stream URL/key setup available.
Support broadcasting over Wi-Fi and mobile data, automatically adapting
outgoing video quality to the active connection's upload conditions.

Acceptance criteria:

- Send live video, audio, and the scoreboard to a selected destination.
- Support Wi-Fi and mobile data as broadcast connections.
- Default to mobile data for broadcasting; retain Wi-Fi as a supported alternative.
- Automatically lower outgoing video quality when upload conditions deteriorate,
  preserving match information and scoreboard updates during quality changes.
- Accept a stream URL and key without requiring in-app platform account login.
- Mask the stream key during normal use and allow the user to replace it.
- Provide a local preview and start/stop controls.
- Show preparing, connecting, streaming, reconnecting, stopped, and error
  states as applicable. A failed connection must not appear as a live stream.
- Display actionable connection errors without exposing credentials.
- Preserve match information during a connection failure. Reconnection
  behavior and retry limits will be defined during technical planning.

**Adaptive quality:** the user requested automatic quality adjustment, using
the YouTube mobile app as a reference for the desired experience. Exact bitrate
limits, supported resolution/frame-rate changes, adaptation thresholds and
timing, and quality-recovery behavior remain technical planning decisions.
Wi-Fi and mobile data availability is confirmed; measured upload speeds and
network-handoff behavior remain to be established.

**Confirmed connection default (owner, 2026-10-18):** default to mobile data.
Venues change, so the owner has no venue-specific upload measurements to supply.
Wi-Fi remains supported. The [M1.4 inventory](plan/inventories/m1-4-venue-upload-baseline.md)
records both measurement gaps and the collection fields for future sessions;
the default does not establish upload capacity or adaptation settings.

**Optional destination sign-in:** This feature is included in the first-release
scope; using it is optional for the user. Initial sign-in integrations target
YouTube and Twitch. Other compatible destinations may use manual setup.

- Let the user connect a destination account through the provider's official
  authorization flow; the app must not collect the account password itself.
- Retrieve or create the connection details automatically after authorization.
  If a provider requires a broadcast to be selected or created, guide the user
  through that step inside the app without requiring manual URL/key retrieval.
- Show the connected account/channel and selected destination before the user
  confirms going live. Signing in must not start a broadcast automatically.
- Let the user disconnect or change the connected account on the streaming
  phone and remove saved authorization when the account is disconnected.
- Handle canceled sign-in, expired or revoked authorization, missing
  permissions, and platform eligibility errors with a clear next step.
- Keep manual URL/key entry available when the user skips sign-in or an
  integration is unavailable; explain that manual entry does not bypass
  destination account restrictions.

Simultaneous destinations remain outside the selected first-release scope.

#### FR-02: Use the Phone's Cameras and Switch Lenses

**User story:** As a streamer, I want to choose the phone's camera lens so I
can frame the volleyball court from my position.

**Required capability:** Capture from the phone's built-in cameras and switch
between available lenses.

Acceptance criteria:

- Capture directly inside this app using Android camera capabilities.
- Identify available cameras and lenses, such as main, ultrawide, telephoto,
  and front-facing cameras, where exposed by the device.
- Allow selection before streaming and supported lens changes during a stream.
- Clearly identify unavailable options; do not claim access to every lens
  present on the phone.
- Preserve the scoreboard and audio configuration when changing lenses.
- Validate stream continuity and any switching interruption on the chosen
  devices before promising seamless switching.

Device-specific lens exposure is a feasibility dependency. Android documents
differences between physical cameras, logical cameras, and manufacturer
implementations. [Android multi-camera documentation](https://developer.android.com/media/camera/camera2/multi-camera).

#### FR-03: Use Built-in and Accessory Microphones

**User story:** As a streamer, I want to use the phone's microphone or a
compatible accessory so the broadcast includes the desired audio.

**Version 1 capability:** Capture the phone's built-in microphone, with visible
audio level, mute and permission/error handling. Under Q48 (2026-10-24), external
wired/Bluetooth microphone support and testing are deferred until after version 1.

Acceptance criteria:

- Capture the phone's built-in microphone and show its active input and a
  level indicator before and during streaming.
- Preserve intentional mute through supported capture/lifecycle changes.
- Report when microphone permission or input access prevents capture.
- Validate built-in captured/received audio and mute on the selected phones.

Post-version-1 accessory criteria, deferred under Q48:

- Detect and allow selection of app-exposed wired/Bluetooth microphone inputs,
  without a preferred accessory brand or model.
- Notify on selected-accessory disconnect and use built-in fallback when
  permitted, preserving intentional mute.
- Validate actual capture/routing separately for USB, other wired, receiver
  and direct Bluetooth chains before claiming their support.

**Confirmed accessory phasing:** the original Q9 wired/Bluetooth request remains
a later feature; Q48 explicitly defers its support and testing until after
version 1. Future compatibility claims still require device/accessory evidence.

**Current validation equipment (owner, 2026-10-18):** built-in microphones only
for now. The [M1.3 inventory](plan/inventories/m1-3-representative-audio-accessories.md)
records the built-in baseline and pending USB, other wired, wireless-receiver,
and Bluetooth cases. The later Q48 decision narrows version 1 to built-in audio;
equipment availability alone never establishes tested support.

#### FR-04: Display an Indoor Volleyball Broadcast Scoreboard

**User story:** As a viewer, I want to see the match information alongside
the video so I can follow the game.

**Required capability:** Show score, team information, sets, timeouts, and serving indicator.

Acceptance criteria:

- Include both teams' abbreviations, current points, compact sets-won count
  (e.g. 2 - 1), each team's timeouts remaining, and an active serving indicator
  in the outgoing video. Retain editable full team names in match information.
- Lock broadcast and overlay rendering to landscape orientation (16:9).
- Render the overlay into the broadcast itself, so viewers see it without
  installing this app.
- Show the same match values in the local preview and controller interfaces.
- Update the overlay when an authorized match edit is accepted.
- Support swapping the visual display order when teams change court sides,
  preserving each team's identity, score, and serve possession.
- Keep abbreviations and numbers readable at the 720p validation baseline;
  define abbreviation length limits and truncation behavior during UI planning.
- Allow configurable scoreboard placement using presets: top-left, top-right,
  bottom-left, bottom-right, and bottom-center.
- Use top-left when the streaming phone has no saved placement preference.
  Save the selected preset on that streaming phone across matches and app
  restarts; confirmed match reset retains it. Each streaming phone has its
  own preference.
- Allow the streaming phone and authorized second Android phone/tablet to
  change placement before or during a live stream. The watch cannot change it.
  Move the complete scoreboard without changing match values or team order.
- Keep placement outside match Undo. A placement change preserves the existing
  match Undo opportunity; Undo reverses the latest match edit while keeping
  the currently selected placement. Change placement back manually on a
  permitted phone/tablet.
- Allow editable team abbreviations of 1–5 characters, validating entry instead
  of silently truncating codes.
- Accept only letters A–Z and digits 0–9 in the final abbreviation. Trim outside
  spaces and show lowercase a–z as uppercase before saving; reject internal
  spaces, punctuation, emoji, and other characters. Validate the resulting
  1–5-character code without truncation.
- Allow both teams to use matching abbreviations after a warning and explicit
  confirmation. Compare prepared codes without regard to case and keep each
  team's stable identity, full name, scores, timeouts, serve, and history separate.
- If a future version restores a saved match whose abbreviations fail the
  current rules, preserve the match and original codes and keep scoring and
  corrections available. Require phone/tablet review and correction before
  starting a broadcast; never silently shorten or replace the saved codes.
- Show each team's timeouts remaining as a number labeled `T/O`, including
  customized timeout allowances.
- When numeric values gain digits, widen or rearrange the complete scoreboard
  within its safe area, keeping full numbers and the reviewed readable text
  size. Exact safe bounds, coverage, and text sizes remain technical decisions.
- If a valid numeric value still cannot fit legibly after layout adjustment,
  stop active broadcasting with a clear notice, preserve the full new match
  value, and keep scoring/corrections available off-air. Do not reject the
  otherwise valid numeric edit or truncate its value to keep publishing.
- Block broadcast start while a known numeric fit failure remains unresolved.
  Resolving it does not automatically restart publishing; normal start
  confirmation and readiness checks apply on every permitted control device.

**Confirmed display choices:** timeouts remaining as numeric `T/O` values,
editable 1–5-character team abbreviations with validation, and configurable
placement presets (owner clarifications, 2026-10-18). Q37–Q39 add the top-left
initial default, phone-specific persistence, and phone/tablet placement controls
before and during a live stream (2026-10-21). Q40 keeps placement outside match
Undo while preserving its current opportunity and the selected position during
Undo. Exact geometry, typography, safe margins, and quality-floor readability
remain D10 work in the
[M1.6 specification](plan/specifications/m1-6-overlay-display-specification.md).
Q41/Q42 settle uppercase A–Z/digit validation and permit matching codes after
a warning and explicit confirmation; stable team identities remain separate.
Q43 preserves nonconforming saved codes for recovery while requiring phone/tablet
correction before broadcasting can start.
A timeout countdown remains outside the MVP, as specified in section 2.3.
Q44/Q45 add adaptive numeric layout and a visible stop with preserved full match
values when numbers cannot fit (owner, 2026-10-23). The
[numeric-fit review](plan/specifications/d10-numeric-fit-and-overflow-policy-review.md)
records the selected policy and N01–N14. Geometry, numeric storage limits,
durable shutdown/recovery sequencing, and actual readability remain open.

#### FR-05: Enter and Correct Match Information

**User story:** As a scorekeeper, I want to enter and correct match information
so the broadcast reflects what happens on court.

**Required capability:** Enter scores, team names, sets, serving possession, and timeouts,
with customizable indoor volleyball presets available from the first release.

Acceptance criteria:

- Edit both teams' names, abbreviations, and starting match values before going live.
- Allow sets-won totals as an explicit starting baseline when earlier set scores
  are unknown; record later sets without inventing earlier scores or win order.
- When those early scores become known, replace the baseline with complete,
  ordered, valid early-set history that matches its totals. Count each set once
  and preserve recorded later play.
- Allow reviewed phone/tablet corrections to starting sets-won totals after
  later play has been recorded only when the resulting sequence remains valid.
  Recompute affected totals and set state consistently; reject corrections that
  invalidate or discard later play and preserve the committed match on rejection.
- Allow an already-completed match to be entered only from a complete ordered
  set history that validates under the selected preset; derive totals from that
  history, retain final-set points, and do not accept totals alone as a completed
  match.
- Increase or decrease points, toggle serving team, swap court sides, edit set
  values, and record or correct timeouts while streaming.
- Start each new set without a selected server and require explicit team
  selection before ordinary rally scoring. Preserve an explicitly supplied or
  durably recorded server for an ongoing set.
- Prevent negative counts and clearly identify invalid input.
- Provide a way to undo an accidental edit without restarting the stream.
- Removing a point leaves serving possession unchanged; Undo restores the recorded
  previous state, including the previous score and server.
- Retain one-step Undo for the latest accepted match edit across app restart;
  use manual correction for earlier mistakes. Undo includes points, serve,
  timeouts, names/codes, display swaps, starting-value corrections, set/match
  confirmation, valid historical corrections, and current-set cap-continuation
  confirmation. A successful Undo consumes the opportunity; do not provide
  Redo or reach an older edit until a new accepted match edit occurs. Exclude
  stream/account actions and confirmed match reset.
- Require deliberate confirmation for resetting the match.
- A confirmed reset retains teams, names/codes, copied match settings, and
  display order; clears points, sets-won baseline/totals, completed history, Undo,
  and cap-continuation approval; and starts set 1 at 0–0 with full configured
  timeouts and no server selected. Keep broadcasting independent unless Q45's
  diagnosed numeric-fit failure requires a visible stop with the full reset
  match retained and scoring available off-air.
- Permit confirmed reset on the streaming phone and an authorized second Android
  phone/tablet; keep reset unavailable on the watch.
- Retain current match information through a controller disconnect, stream
  interruption, or app restart.
- Let the user customize set targets (default 25), deciding-set targets (default 15),
  winning margin (default 2), sets needed to win (default 3, best of 5; or best of 3),
  and timeouts allowed per team per set (default 2).
- Use preset values to identify a potential set or match winner, while
  requiring confirmation before advancing or ending a match.
- A point cap does not override the configured winning margin (default 2).
  At target 25, margin 2, and cap 30, a 30–29 score is not eligible for set
  confirmation. Require an explicit resolution when the cap conflicts with
  the margin: after the operator confirms "continue win by two" for that set,
  allow scoring beyond its cap while preserving the winning margin. Later
  sets retain their configured cap. Pause ordinary rally input at the conflict
  until that confirmation or a correction; cancellation leaves it unresolved.
- Permit deliberate current-set cap-continuation confirmation on the streaming
  phone and authorized second Android phone/tablet. The watch shows the paused
  conflict and directs the operator to a phone; it cannot confirm continuation
  or Undo that latest confirmation, and must never skip it to Undo an older edit.
- Require a configured cap to be at least the applicable set target. For
  starting/restored scores, enter the first cap-reaching one-point lead as the
  same paused cap conflict; require explicit acknowledgement that the current
  set already continued win by two before accepting a tied-at-cap or above-cap
  score. Retain that acknowledgement only for the current set.
- Pause ordinary rally scoring at a potential winner until confirmation or
  correction; keep score corrections available.
- Allow manual corrections, including after confirming a set, and update
  related set totals consistently.
- Reject a historical correction that makes later play inconsistent, preserving
  recorded play and requiring a coherent reviewed correction instead.
- When a valid reviewed correction to a completed match leaves neither team at
  the required sets-won threshold, reopen the match for its required next set.
  Preserve the corrected history, identities, configuration, and display order;
  start that set 0–0 with the full configured timeout allowance and no active
  server until the operator selects one. Keep streaming independent.

Approved default values are listed in section 2.3. Users may customize them
for their event; the app does not replace the official scorekeeper or referee.

#### FR-06: Control from a Second Mobile Device and Wearable

**User story:** As a scorekeeper, I want to control the session remotely so
I can leave the camera phone positioned toward the court.

**Required capability:** Provide controls on the streaming phone, a second
Android phone or tablet, and a Wear OS watch. Remote controls include scores,
sets, timeouts, team names, serving indicator, court side swap, camera selection,
microphone mute, and confirmed stream start/stop, with simpler controls on the watch.

Acceptance criteria:

- Pair an authorized controller with the streaming phone.
- Show current scores, set information, timeouts, serving possession, and connection
  status on the controller.
- Support the device control matrix in section 2.4.
- Allow local controls, the second Android device, and the watch to participate
  in the same session, using the streaming phone's accepted command order.
- Acknowledge accepted changes and show failures or a disconnected state.
- Prevent a retried command from accidentally adding a point twice.
- After reconnecting, refresh the controller from the streaming phone's
  current match state before accepting further edits.
- Continue streaming and local scoring when the controller disconnects.
- Allow the streaming phone to revoke a controller's access.

Wear OS support is part of the first release. The selected watch pairing
topology and connection method are recorded in decision Q7 in section 5.3;
device compatibility and implementation feasibility require validation.

### 2.3 Indoor Volleyball Scoring Rules & Presets

The initial release concentrates on customizable indoor volleyball scoring formats.
Beach volleyball is deferred to future releases. The approved starting defaults
follow FIVB's 2025-2028 indoor volleyball scoring and timeout rules:

| Setting | Indoor standard (Best of 5) | Indoor variant (Best of 3) |
| --- | --- | --- |
| Sets needed to win | 3 | 2 |
| Regular-set points | 25 | 25 |
| Deciding-set points | 15 (Set 5) | 15 (Set 3) |
| Winning margin | 2 points | 2 points |
| Point cap | None (customizable) | None (customizable) |
| Team timeouts per set | 2 per team | 2 per team |
| Court side change | Between sets, and at 8 pts in deciding set | Between sets, and at 8 pts in deciding set |

Sources: FIVB rules 6.2, 6.3, and 15.1 in the
[indoor rules](https://www.fivb.com/wp-content/uploads/2025/01/FIVB-Volleyball_Rules2025_2028-EN-v05.pdf).
Beach volleyball rules are deferred to the post-MVP roadmap.

Preset behavior:

- Let users customize the values before a match (e.g., set targets, deciding-set targets, cap, timeout allowances).
- Keep each match's configuration separate from the preset's starting defaults.
- Reset each team's timeout count when a new set is confirmed.
- Preserve completed-set scores when starting the next set.
- Preserve team identity, points, sets, and timeout ownership when the display
  order is swapped after teams change court sides.
- Automatically toggle serving possession on sideout points, with manual override.
- Treat competition-specific technical timeouts separately from team-requested
  timeouts. Automatic technical-timeout handling and countdown timers remain
  outside the MVP; these presets cover score and team-timeout counts.

### 2.4 Device Control Matrix

The user approved full phone controls and a simpler watch interface, with
the following allocation:

| Control | Streaming Android phone | Second Android phone/tablet | Wear OS watch |
| --- | --- | --- | --- |
| View match and stream status | Yes | Yes | Yes |
| Add/remove a point and undo an edit | Yes | Yes | Yes |
| Select first server / toggle serving team (possession) | Yes | Yes | Yes |
| Record/correct a team timeout | Yes | Yes | Yes |
| Confirm a set result | Yes | Yes | Yes |
| Confirm current-set cap continuation | Yes | Yes | No |
| Swap court sides / display order | Yes | Yes | Yes |
| Reset the match with confirmation | Yes | Yes | No |
| Edit team names, abbreviations, and historical set values | Yes | Yes | View only |
| Choose/customize a scoring preset | Yes | Yes, before the match | View only |
| Change scoreboard placement before/during a live stream | Yes | Yes | No |
| Select an available camera/lens | Yes | Yes | Defer from the initial watch interface |
| Mute/unmute the microphone | Yes | Yes | Yes |
| Start/stop the stream with confirmation | Yes | Yes | Yes |
| Sign in to a destination, manage stream credentials, and authorize controllers | Yes | No | No |

Remote start depends on the streaming phone being ready and permitted to
capture; it does not bypass Android permission or lifecycle restrictions.
Q43's restored-code readiness restriction also applies to all stream-start
controls: a remote or watch request cannot bypass required phone/tablet code correction.

Q36 also restricts Undo of the latest cap-continuation confirmation to a
phone/tablet. The watch shows the paused conflict or directs that restricted
Undo to a phone; it never skips the latest edit.

Q37–Q39 settle scoreboard placement: use top-left without a saved preference;
retain each streaming phone's selection across matches, restarts, and confirmed
match reset. Allow placement changes before/during a broadcast on the streaming
phone and authorized second Android phone/tablet; deny changes from the watch.
Under Q40, placement is outside match Undo: changing it preserves the current
match Undo opportunity, and Undo keeps the selected position. Q27 still controls
watch Undo of the latest match edit; this grants no watch placement control.

### 2.5 Non-Goals for the First Release

These approved exclusions keep the personal project manageable:

- Beach volleyball scoring rules and side-switch cadence (deferred to later releases; indoor volleyball is the initial focus).
- Automatic score recognition, computer vision, or referee decisions.
- Multiple camera phones, instant replay, or a production switching console.
- Tournament administration, player statistics, or roster management.
- Social features, monetization, or a custom viewer platform.
- Local video recording, cloud match history, and advanced overlay themes.
- Simultaneous broadcasting to multiple destinations (streaming to one destination at a time).
- Third-party destination sign-in beyond YouTube and Twitch (e.g., Kick, Facebook Live); compatible services use manual RTMP/RTMPS entry.
- Browser/iOS controllers and Apple Watch support (Android and Wear OS only).

## 3. AI System Requirements (If Applicable)

Not applicable to the current requirements. Users enter match information
manually; no AI product feature is proposed for the first release.

## 4. Technical Specifications

### 4.1 Proposed Architecture

This is a conceptual flow, not a selected implementation stack:

- Phone camera frames and scoreboard graphics form the outgoing video.
- The active microphone supplies the outgoing audio.
- An encoding and publishing component sends audio/video to the destination.
- An optional destination account integration obtains publishing details and
  manages the authorization needed for automatic setup.
- The streaming phone owns the current match state and persists it to local
  storage (e.g., Jetpack DataStore or SQLite) upon each accepted edit to ensure
  continuity through unexpected app recreation or crash.
- Local actions and authorized remote commands update that state, then update
  the overlay and controller displays.

Language, UI toolkit, minimum Android version, capture and streaming libraries,
storage, controller transport, and Wear OS UI framework are **TBD**.

### 4.2 Integration Points and Constraints

- **Target hardware:** one Pixel 8 Pro, two Pixel 10 phones, two Pixel 10 Pro
  phones, and four Pixel Watch 4 watches are available. Each Pixel 10-series
  phone has its own paired watch. The main test group is one Pixel 10 Pro,
  its watch, and the Pixel 8 Pro; any available device may be tested. The owner confirmed
  on 2026-10-18 that phone roles are interchangeable: each selected phone may
  stream or act as the Android controller, with no permanent assignment.
  The expected software is the latest published stable OS version or the
  previous major stable OS version (N−1). Record actual installed Android/Wear OS
  versions, builds, and the roles used for each validation session. Validate
  camera, microphone, stream continuity,
  and remote-control behavior on the selected physical devices.
- **Streaming:** support optional destination sign-in with automatic setup and
  manual stream URL/key entry for one destination at a time. Lock camera capture
  and overlay rendering to landscape orientation (16:9). Automatically adapt
  outgoing quality to upload conditions over Wi-Fi or mobile data; evaluate
  encoder/publisher support for quality changes with the scoreboard included.
  Define the quality range, adaptation timing, recovery policy, and behavior
  when changing between Wi-Fi and mobile data during technical planning.
  Evaluate RTMPS
  where the destination supports it and define compatibility with other supported
  ingest endpoints. YouTube documents RTMPS ingestion, and Twitch documents its
  broadcast ingest workflow. [YouTube RTMPS guide](https://developers.google.com/youtube/data/live/guides/rtmps-ingestion),
  [Twitch broadcast guide](https://dev.twitch.tv/docs/video-broadcast/).
- **Destination accounts:** evaluate provider-supported authorization and APIs
  for retrieving or creating publishing details and any required broadcast
  setup. YouTube exposes ingestion information in its live-stream resource and
  requires creating and binding a liveBroadcast resource; Twitch exposes an
  authorized Get Stream Key endpoint paired with ingest server discovery
  (`https://ingest.twitch.tv/ingests`). Provider app registration, permissions,
  token handling, quotas, and any required review must be resolved during
  technical planning. [YouTube live-stream resource](https://developers.google.com/youtube/data/live/reference/rest/v3/liveStreams),
  [Twitch Get Stream Key](https://dev.twitch.tv/docs/api/reference/#get-stream-key).
- **Capture & thermal mitigation:** evaluate Android camera and audio APIs
  against the selected hardware. Provide a display-dimming / low-power screen
  mode while streaming to mitigate thermal throttling and battery consumption
  during long (90–120 min) indoor matches. Choosing a camera skill does not
  select the application's library.
- **Remote connection:** recommend starting with a nearby controller and
  avoiding a required custom cloud service. LAN, phone hotspot, and Bluetooth
  feasibility must be evaluated against the selected devices and gym setup.
- **Wearable connection:** decision Q7 selects direct pairing to the streaming
  phone using Wear OS Data Layer. Validate this topology on the selected phone
  and watch during technical planning; any required scope change must be
  confirmed with the user. [Wear OS Data Layer overview](https://developer.android.com/training/wearables/data/overview).
- **Lifecycle:** define behavior for screen lock, backgrounding, incoming calls,
  audio focus loss, low battery, and thermal limits during technical planning.

### 4.3 Security and Privacy

- Require explicit authorization when pairing a remote controller.
- Keep streaming credentials on the streaming phone; do not expose them to
  scoring controllers, screenshots of setup instructions, or logs.
- Store destination authorization tokens and saved stream credentials using
  platform-protected storage. Do not embed a provider client secret in the
  distributed app; select an authorization design suitable for a mobile app.
- Request only the provider permissions needed for streaming setup and
  control. Define token renewal, reauthorization, and account disconnection.
- Request camera and microphone access when needed and show capture status.
- Avoid a mandatory app account or a custom backend unless a later scope
  decision requires one.
- Make stream start/stop and match-reset actions deliberate. Require
  confirmation for remote stream start/stop.

## 5. Risks & Roadmap

### 5.1 Release Plan

| Stage | Outcome |
| --- | --- |
| Requirements - approved | PRD accepted by the project owner on 2026-10-17; explicitly open implementation details remain unresolved. No application code. |
| Technical planning - after requested | Select target hardware and technologies; plan checks for destination authorization and automatic setup, streaming, lens switching, audio routing, overlay rendering, and controller connectivity. |
| MVP implementation - after requested | Deliver all six capabilities, optional destination sign-in plus manual setup, customizable indoor volleyball scoring presets, second Android device controls, and Wear OS controls. |
| Validation | Run the approved success checks, including signed-in and manual destination setup, and a full 90–120 minute indoor match rehearsal on supported devices. |
| Later iterations | Add external wired/Bluetooth microphone support and testing under Q48; consider beach volleyball presets and side-switch reminders, additional destinations/sign-in, simultaneous streaming, recording and visual customization. |

### 5.2 Main Risks

| Risk | Planning response |
| --- | --- |
| Phone does not expose a desired lens or cannot switch it during capture | Test exposed capabilities on the selected Pixel phones before promising behavior. |
| Accessory audio routing differs across devices | Deferred until after version 1 under Q48; validate actual routes and fallback before claiming accessory support. Built-in audio remains a version 1 physical check. |
| Gym connectivity interrupts the stream | Use automatic quality reduction for deteriorating upload conditions; define visible connection status, network-handoff behavior, and bounded retries; preserve score state independently of the stream. |
| Sustained video processing overheats the phone or drains its battery | Validate the proposed quality, full-match duration (90–120 min), and screen-dimming mode on the target phone. |
| Controller disconnects, duplicates a command, or displays stale data | Specify acknowledgments, duplicate handling, and resynchronization before implementation. |
| Platform setup or account restrictions prevent broadcasting | Verify current destination prerequisites and document setup before the first live test. |
| Provider authorization, API limits, or app review blocks automatic setup | Validate each integration's requirements before implementation; provide actionable errors, reauthorization, and manual connection setup. |
| Watch compatibility expands the personal project's scope | Keep the initial platform to Wear OS and validate the approved control set on the selected watch. |

### 5.3 Confirmed Scope Decisions

The user answered the initial clarification questions on 2026-10-17. Each
question provided a recommended answer and an Other/custom-answer path.
The streaming workflow below also includes the user's subsequent request for
optional destination sign-in, and the decision to focus on indoor volleyball.
The user subsequently confirmed target devices, accessory microphone scope,
scoreboard display preferences, and project constraints on 2026-10-17.
The user also selected adaptive streaming quality and confirmed availability
of both Wi-Fi and mobile data.
On 2026-10-18, the owner confirmed interchangeable phone roles and the expectation
of the latest published stable OS version or the previous major stable OS
version (N−1). A later clarification on the same date confirmed two regular
Pixel 10 phones and two Pixel 10 Pro phones, each with its own Pixel Watch 4,
plus one Pixel 8 Pro without a current watch setup. Any device is available
for testing; the main test group is one Pro, its watch, and the Pixel 8 Pro. The
[M1.2 inventory](plan/inventories/m1-2-device-roles-and-os-inventory.md) records
these answers separately from unobserved software/builds and untested capabilities.
During M1.4 on 2026-10-18, the owner reported that venues keep changing and no
venue-specific upload measurements are available, and selected mobile data as
the default broadcast connection. Wi-Fi support remains in scope.

During M1.5 on 2026-10-18, the owner confirmed point-removal behavior, rejection
of conflicting historical corrections, and one-step Undo retained across restart.
The [worked specification](plan/specifications/m1-5-match-correction-specification.md)
separates these answers from the remaining D11 proposals.

During M1.6 on 2026-10-18, the owner requested configurable placement, then chose
preset positions (a corner or bottom-center), editable 1–5-character codes with
validation, and remaining timeout numbers labeled `T/O`. The
[M1.6 specification](plan/specifications/m1-6-overlay-display-specification.md)
separates those choices from remaining D10 details and future physical evidence.

During the D11 follow-up on 2026-10-20, the owner chose to preserve the winning
margin when a cap conflicts, allow totals-only starting sets as a baseline,
and pause rally scoring at a potential winner while keeping corrections
available. A follow-up answer selected explicit confirmation to continue win
by two beyond the cap for the current set only. Reaching 30–29 under a cap of
30 does not authorize a one-point win or automatic continuation; later sets
retain their configured cap.

| ID | Topic | User's decision |
| --- | --- | --- |
| Q1 | Streaming workflow | One destination at a time; optional destination sign-in automatically configures the connection, with manual stream URL/key entry also available. Initial sign-in targets are YouTube and Twitch; other compatible services retain manual setup. |
| Q2 | Control devices | The streaming device, a second Android phone or tablet, and a Wear OS watch. |
| Q3 | Remote controls | Scores, sets, timeouts, team names, serving indicator, court side swap, camera selection, microphone mute, and confirmed stream start/stop; simpler controls on a watch. |
| Q4 | Scoring formats | Concentrate on indoor volleyball (customizable presets for best of 5 and best of 3); beach volleyball is deferred to later releases as a rarely used format. |
| Q5 | Scoreboard set format | Compact sets counter: show current set points and sets-won counter (e.g. 2 - 1), keeping the overlay minimal and readable at 720p. |
| Q6 | Serving indicator | Active serving indicator (e.g., a dot or accent) that toggles automatically on sideout points, with manual override on controllers. |
| Q7 | Wear OS connection | Direct pairing to the streaming phone via standard Wear OS Bluetooth/companion pairing (using Wear OS Data Layer directly). |
| Q8 | Target devices | Confirmed quantities on 2026-10-18: one Pixel 8 Pro, two regular Pixel 10 phones, two Pixel 10 Pro phones, and four Pixel Watch 4 watches (one paired with each Pixel 10-series phone). Phone roles are interchangeable. Any device may be tested; main testing uses one Pro, its watch, and the Pixel 8 Pro. Actual installed software/builds remain unknown. |
| Q9 | Accessory microphones | Original request: any wired or Bluetooth microphone, without a preferred brand/model, subject to physical validation. Q48 later defers support/testing until after version 1. |
| Q10 | Timeout display | Show each team's timeouts remaining. |
| Q11 | Team display | Use team abbreviations on the scoreboard; retain full team names in match information. |
| Q12 | Project constraints | No additional user-imposed budget, deadline, technology, or distribution constraints. Implementation choices remain open. |
| Q13 | Streaming quality | Automatically adapt outgoing stream quality to the active connection, lowering quality when upload conditions deteriorate. The YouTube mobile app is the user's experience reference; exact adaptation parameters remain technical planning decisions. |
| Q14 | Gym connectivity | Both Wi-Fi and mobile data are available. On 2026-10-18 the owner selected mobile data as the default broadcast connection because venues keep changing; no venue-specific measurements are available. Wi-Fi remains supported, and upload speeds remain unknown in the project record. |
| Q15 | Expected device software | Confirmed 2026-10-18: devices are expected to have the latest published stable OS version or the previous major stable OS version (N−1). This expectation does not establish installed versions, minimum SDKs, or tested compatibility. |
| Q16 | Point removal and serve | Confirmed 2026-10-18: removing a point leaves serving possession unchanged. Undo restores the recorded previous score and server. |
| Q17 | Conflicting historical corrections | Confirmed 2026-10-18: reject a correction that makes later play inconsistent, keeping recorded play intact and requiring a coherent reviewed correction. No automatic deletion of later play is approved. |
| Q18 | Undo depth and restart | Confirmed 2026-10-18: one-step Undo for the latest match edit, available after app restart; earlier mistakes use manual correction. Q26/Q27 later settle action scope, repeat behavior, and watch authority; technical retention remains D11 work. |
| Q19 | Scoreboard placement | Confirmed 2026-10-18: configurable preset positions — top-left, top-right, bottom-left, bottom-right, and bottom-center. Later Q37–Q39 settle the initial default, persistence, and live/remote permissions; the [focused review](plan/specifications/d10-placement-control-policy-review.md) records those answers. |
| Q20 | Abbreviation length | Confirmed 2026-10-18: editable 1–5-character team codes, with entry validation instead of silent truncation. Full names remain separate and editable in match information. Q41/Q42 later settle characters/casing and duplicate permission. |
| Q21 | Timeout representation | Confirmed 2026-10-18: each team's timeouts remaining appear as a number labeled `T/O`, for both default and customized allowances. |
| Q22 | Cap versus winning margin | Confirmed 2026-10-20: preserve the winning margin; a 30–29 score with target 25, margin 2, and cap 30 is not a potential set winner. Require explicit resolution of a cap/margin conflict; Q25 selects current-set continuation after confirmation. No automatic margin waiver or cap override is approved. |
| Q23 | Mid-match starting set totals | Confirmed 2026-10-20: allow totals-only starting sets as an explicit baseline when earlier scores are unknown; record later sets separately without inventing earlier scores or win order. Q28 later resolves completed-match entry separately. |
| Q24 | Rally input at a potential winner | Confirmed 2026-10-20: pause ordinary rally scoring until the potential result is confirmed or corrected. Keep corrections available and require confirmation before the next set. |
| Q25 | Cap-conflict resolution | Confirmed 2026-10-20: resolve the cap conflict with explicit "continue win by two" confirmation before ordinary rally scoring resumes and may exceed the cap. Corrections remain available. The exception applies only to the current set and preserves the winning margin; later sets keep their configured cap. Canceling does not authorize continuation. |
| Q26 | Undo scope and repeat | Confirmed 2026-10-21: Undo covers the latest accepted match edit across devices: points, serve, timeouts, names/codes, display swaps, starting-value corrections, set/match confirmation, valid historical corrections, and current-set cap-continuation confirmation. Exclude stream/account actions and confirmed match reset. A successful Undo consumes the opportunity; another Undo does nothing until a new accepted match edit. |
| Q27 | Watch Undo authority | Confirmed 2026-10-21: the watch may Undo the latest edit from any device when that edit is within the watch's allowed match controls: point, serve, timeout, display-swap, or set-confirmation edit. For a names/history or other restricted edit, show that a phone is required; never skip it to Undo an older edit. This grants no direct permission for restricted edits; Q36 separately confirms phone/tablet-only cap continuation and Undo of that latest confirmation. |
| Q28 | Completed-match entry | Confirmed 2026-10-21: allow setup of an already-finished match only with a complete ordered set history whose scores validate under the selected preset. Derive totals from the supplied history; retain the final recorded set and points; do not create an active next set, server, or timeout state. Reject totals-only completed entry rather than inventing scores or winner order. |
| Q29 | Cap-input and restoration | Confirmed 2026-10-21: require a cap to be at least the applicable target. A first cap-reaching one-point lead, such as 30–29 with target 25, margin 2, cap 30, enters the paused cap-conflict state. Accept tied-at-cap or above-cap starting/restored input only after the operator explicitly acknowledges prior current-set continuation; otherwise reject it rather than infer authorization. Retain continuation only for the current set; a durable restored match uses its recorded continuation state. |
| Q30 | Completed-match correction and reopening | Confirmed 2026-10-21: review a coherent historical correction on a phone or Android tablet before committing it. If a valid correction to a completed match leaves neither team at the required sets-won threshold, reopen at the required next set with points 0–0, the full configured timeout allowance, and no active server until the operator selects one. Preserve corrected history, team identities, configuration, and display order; keep streaming independent. Reject invalid corrections and preserve the committed match. |
| Q31 | Replace a totals-only baseline with known history | Confirmed 2026-10-21: when missing early-set scores become known, replace the baseline with complete, ordered, valid early-set history that matches its totals. Count each set once and preserve later play; do not invent results or add the same wins again. |
| Q32 | Correct starting totals after later play | Confirmed 2026-10-21: allow a reviewed phone/tablet correction to starting sets-won totals after later play has been recorded only when the resulting set sequence remains valid. Recompute affected totals and set state consistently; reject changes that invalidate or discard later play and preserve the committed match on rejection. |
| Q33 | First server for each new set | Confirmed 2026-10-21: start with no server selected and require explicit team selection before ordinary rally scoring. Do not carry forward the previous set's last server. Existing permitted serve controls on authorized phones/tablets and the watch provide that selection. An ongoing set retains its explicitly supplied or durably recorded server. |
| Q34 | Confirmed reset contents | Confirmed 2026-10-21: retain teams, names/codes, the match's copied settings, and current display order. Clear points, sets-won baseline/totals, completed history, Undo, and cap-continuation approval; start set 1 at 0–0 with full configured timeouts and no selected server. Keep the broadcast independent; no Undo across reset under Q26. |
| Q35 | Reset authority | Confirmed 2026-10-21: allow deliberate confirmed match reset on the streaming phone and authorized second Android phone/tablet. Keep reset unavailable on the watch; its existing serve/scoring permissions remain unchanged. |
| Q36 | Cap-continuation controller permissions | Confirmed 2026-10-21: the streaming phone and authorized second Android phone/tablet may deliberately confirm current-set "continue win by two". The watch shows the paused conflict and directs the operator to a phone; it cannot confirm continuation or Undo that latest phone-only confirmation. Never skip it to Undo an older edit. Q26's latest-edit Undo remains available on permitted phones/tablets; setup/history/reset permissions remain separate. |
| Q37 | Initial scoreboard placement | Confirmed 2026-10-21: use top-left when the streaming phone has no saved placement preference. All five Q19 presets remain available. |
| Q38 | Placement persistence | Confirmed 2026-10-21: save the selected placement on the streaming phone across matches and app restarts; confirmed match reset retains it. Each streaming phone keeps its own preference. |
| Q39 | Placement controller permissions and live changes | Confirmed 2026-10-21: allow the streaming phone and authorized second Android phone/tablet to change placement before or during a live stream. The watch cannot change it. Q40 separately settles placement's interaction with match Undo. |
| Q40 | Placement and match Undo | Confirmed 2026-10-21: keep placement outside match Undo. Changing placement preserves the current Undo opportunity; Undo reverses the latest match edit and keeps the selected position. Change placement back manually on a permitted phone/tablet. A placement change does not create a match Undo opportunity when none is available. |
| Q41 | Abbreviation characters and casing | Confirmed 2026-10-21: use 1–5 uppercase letters A–Z or digits 0–9. Trim outside spaces and show lowercase a–z as uppercase before saving; reject internal spaces, punctuation, emoji, and other characters instead of truncating or transliterating them. Count the final accepted letters/digits. Full names remain separate. |
| Q42 | Matching team abbreviations | Confirmed 2026-10-21: allow matching codes, comparing without regard to case, after a warning and explicit confirmation. Keep teams separate by stable identities; never merge their scores, full names, timeouts, serve, or history. Duplicate permission does not waive Q41 validation or grant watch code editing. |
| Q43 | Restoring nonconforming saved abbreviations | Confirmed 2026-10-21: preserve the saved match and original codes, keep scoring/corrections available, and require a phone/tablet to review and fix invalid codes before starting a broadcast. Never silently shorten or replace them. This is a future recovery policy, not an import feature or permission to discard match data. |
| Q44 | Numeric layout growth | Confirmed 2026-10-23: when numbers grow, widen or rearrange the complete scoreboard within its safe area, keeping complete numeric values and the reviewed readable text size. Exact geometry, coverage, and type limits remain technical decisions. |
| Q45 | Numeric values that cannot fit legibly | Confirmed 2026-10-23: if a valid numeric value still cannot fit after layout adjustment, stop broadcasting with a clear notice, preserve the full new match value, and let scoring continue off-air. Do not reject that otherwise valid numeric edit to keep publishing. A known unresolved fit failure blocks broadcast start; normal confirmation/readiness apply before a later restart. Stop sequencing and recovery remain technical work. |
| Q46 | M2 execution scope | Confirmed 2026-10-23: resume and execute all M2 feasibility/preparation, including its isolated probe/harness work. The owner subsequently reported no physical devices available now and directed continued research/preparation with measured feasibility pending. This does not establish M2 completion or pass G2. |
| Q47 | Public test boundaries | Confirmed 2026-10-23: approve T01–T06 as documented: match commands/real-store recovery, session/status/quality requests, provider setup results, authorized controller commands/results, overlay input/rendered output, and visible phone/watch interactions. Tests observe public behavior; physical compatibility and real broadcasts require separate evidence. |
| Q48 | Version 1 audio and external microphones | Confirmed 2026-10-24: use the phone's built-in microphone for version 1; defer external wired/Bluetooth microphone support and testing until after version 1. External microphone checks do not block M2 or version 1. Built-in audio, camera/watch/network and provider validation remain required. |

### 5.4 Details to Confirm Before Implementation

- Remaining scoreboard details under D10: technical preference/confirmation
  contracts, exact safe margins/block bounds/font
  sizes/contrast, actual character fit, numeric capacity, durable recovery/stop
  mechanisms, and readability at the eventual D07 quality floor.
  The [M1.6 specification](plan/specifications/m1-6-overlay-display-specification.md)
  supplies a static illustration and review criteria. Q19–Q21 and Q37–Q45 are confirmed;
  the [placement/control review](plan/specifications/d10-placement-control-policy-review.md)
  records Q37–Q40, PL01–PL10, and PU01–PU08 for default/persistence/controller
  behavior and placement's exclusion from match Undo;
  candidate geometry and a successful static render do not establish encoded
  or received-broadcast readability.
  The [abbreviation-validation review](plan/specifications/d10-abbreviation-validation-policy-review.md)
  records confirmed Q41/Q42 and AV01–AV11: uppercase A–Z/digit validation and
  matching codes allowed after a warning and explicit confirmation. Its Q43
  recovery section and RC01–RC08 preserve nonconforming saved codes for correction
  while blocking broadcast start. Recovery/reviewed-state mechanisms and actual
  rendering/readability still require their own disposition/evidence.
  The [2026-10-23 numeric-fit review](plan/specifications/d10-numeric-fit-and-overflow-policy-review.md)
  records Q44/Q45 and N01–N14: widen/rearrange within the safe area; if a valid
  numeric value still cannot fit, stop broadcasting with notice, preserve the
  full new match value, and keep scoring available off-air. The rejection
  alternative was not selected. Numeric bounds, exact layout, durable stop
  sequencing, and quality-transition recovery remain open. No two-digit
  scoring limit or new point cap is introduced.
- Remaining D11 technical details: revision/atomic reconciliation and reset
  identity mechanisms; and command
  expiry/receipt retention.
  The [M1.5 specification](plan/specifications/m1-5-match-correction-specification.md)
  records worked examples and explicit open choices. Q16–Q18 and Q22–Q36 are confirmed;
  the other proposals are not approved merely by documentation completion.
  Its [2026-10-20 focused review](plan/specifications/m1-5-match-correction-specification.md#focused-d11-policy-review-2026-10-20)
  records four follow-up answers and corrected cases; the later reviews below
  record the subsequent correction, serve, reset, and controller answers.
  Unselected technical mechanisms remain proposals.
  The [2026-10-21 Undo/watch review](plan/specifications/d11-undo-and-watch-policy-review.md)
  supplies the confirmed action-coverage matrix and twelve cases. Q26/Q27 settle
  scope/repeat behavior and watch authority; Q18 remains the confirmed one-step
  depth and restart requirement.
  The [completed-match entry review](plan/specifications/d11-completed-start-policy-review.md)
  records Q28's confirmed completed-match entry policy and C01–C07 cases.
  The [cap-input review](plan/specifications/d11-cap-input-policy-review.md)
  records Q29's confirmed cap-input policy and K01–K09 cases.
  The [historical-correction review](plan/specifications/d11-history-reopen-policy-review.md)
  records Q30's confirmed coherent-correction and conditional-reopening policy
  with H01–H06 cases.
  The [baseline/history review](plan/specifications/d11-baseline-history-policy-review.md)
  records Q31/Q32 and B01–B12: replacing unknown early history
  without double counting, and correcting starting totals while preserving valid
  later play. Both policies were confirmed on 2026-10-21; storage, revision,
  and atomic-commit mechanisms remain technical decisions.
  The [new-set serve/reset review](plan/specifications/d11-new-set-serve-and-reset-policy-review.md)
  records Q33–Q35 and S01–S14 for explicit first-server selection, retained reset
  fields, and confirmed reset on phones/tablets only. These policies were
  confirmed on 2026-10-21; technical storage/revision/identity mechanisms remain open.
  The [cap-continuation permissions review](plan/specifications/d11-cap-continuation-permissions-review.md)
  records Q36 and CP01–CP10: permitted phones/tablets confirm live continuation
  and may Undo its latest confirmation; the watch directs both actions to a
  phone and never skips a restricted latest edit. Q29 setup acknowledgement and
  existing watch setup/history/reset restrictions remain separate. Technical
  identity/revision/receipt/storage mechanisms remain open.
- Actual installed Android/Wear OS versions and build numbers for each physical
  device remain unknown. Interchangeable phone roles are confirmed; record the
  actual streamer/controller combination per validation session.
- Verify the owner-reported companion pairings on physical devices before watch
  validation. Each of the two regular Pixel 10 and two Pixel 10 Pro phones has
  its own Pixel Watch 4; record the selected physical units for each session.
  When a different phone streams, Q7 requires any watch used for controls to
  be paired directly to that streaming phone. Installed software and actual
  Data Layer behavior still need physical evidence.
- Post-version-1 accessory work under Q48: select representative wired and
  Bluetooth microphones and validate each actual connection chain. There is
  no preferred model. These cases do not block M2 or version 1; retain them in the
  [M1.3 inventory](plan/inventories/m1-3-representative-audio-accessories.md).
- Adaptive-quality limits and strategy: bitrate range, supported resolution
  and frame-rate adjustments, response thresholds/timing, recovery behavior,
  and Wi-Fi/mobile-data handoff policy. Automatic quality adjustment and both
  connection types are confirmed; measured upload speeds remain unknown.
  The stable-connection validation baseline remains 720p/30 fps.
  The [M1.4 upload inventory](plan/inventories/m1-4-venue-upload-baseline.md)
  records separate Wi-Fi/mobile measurement gaps and fields for future venue
  observations. Its planning completion does not supply measured capacity or
  settle adaptation parameters or handoff behavior. Mobile data is the confirmed
  default; observe it first at representative sessions and retain separate
  Wi-Fi cases when available rather than assuming a fixed venue baseline.
- Provider authorization flows, app registration/review requirements, and the
  broadcast selection/creation steps needed for automatic destination setup.
  On 2026-10-23 the owner reported existing registrations but unknown nonsecret
  client/API/consent/signing/quota/review/eligibility facts. The
  [provider execution update](plan/specifications/m2-3-provider-setup-feasibility.md#execution-update-2026-10-23)
  records those gaps without assuming a configured or authorized client.
- Technology selections and the distribution method; the user has imposed no
  additional constraints on these choices.
  The [M2 ledger](plan/specifications/m2-7-stack-and-decision-dispositions.md)
  distinguishes a working isolated JVM toolchain from the proposed application
  stack. T01–T06 are now confirmed under Q47; final Android/watch tool selection,
  physical measurements and D01–D13 evidence remain unresolved as recorded.

These details remain unresolved after PRD approval and the subsequent
clarifications. No budget or deadline constraint was specified. Target-device
selection does not establish a hardware compatibility guarantee or a final
technology choice for items marked TBD.

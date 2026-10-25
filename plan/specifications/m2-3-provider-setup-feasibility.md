# M2.3 provider setup: documentation findings and validation handoff

Researched: 2026-10-20 (America/New_York)  
Status: Documentation research complete; overall provider feasibility in progress.  
Task: [M2.3](../subplans/m2-3-provider-setup-feasibility.md)  
Authority: [FR-01](../../PRD.md#fr-01-livestream-to-an-external-platform), [PRD security](../../PRD.md#43-security-and-privacy)  
Open decisions: [D06 and D12](../MASTER_PLAN.md#7-open-decision-register)  
Design: [Destination setup](../../ARCHITECTURE.md#8-destination-setup-and-authorization)

## Execution update, 2026-10-23

The owner authorized M2 execution and confirmed T01–T06, superseding the
historical authorization/boundary status in the original research below.
Registrations are owner-reported to exist, but the follow-up answer for all
requested nonsecret configuration facts was "unknown". Record Google Android
client type, enabled API, consent/testing audience, signing match, quota/review
and channel eligibility as unknown. Twitch client type, authorization/redirect
configuration and channel eligibility are likewise unknown. No console was
inspected and no registration or authorization was changed.

Rechecked Google's authorization/live-resource documentation and Twitch's
device/refresh guides on 2026-10-23. The separate provider paths remain the
proposals below. Google distinguishes authentication from API authorization;
the Live API uses broadcast and stream resources.
[Android authorization](https://developer.android.com/identity/authorization),
[YouTube overview](https://developers.google.com/youtube/data/live/getting-started),
[stream resource](https://developers.google.com/youtube/data/live/reference/rest/v3/liveStreams).
Twitch's Public device section still describes secret-free rotating refresh,
while the general refresh guide's introductory restriction remains inconsistent.
Keep refresh lifetime/suitability unresolved until observed or clarified.
[Device grant](https://dev.twitch.tv/docs/authentication/getting-tokens-oauth/#device-code-grant-flow),
[refresh guide](https://dev.twitch.tv/docs/authentication/refresh-tokens/).

P01–P12 remain unrun; no token, stream key, provider resource, ingest or received
broadcast evidence exists. [M2 readiness](../reviews/m2-readiness-and-execution.md)
tracks the physical/account handoff. M2.3/D06/D12 remain In progress/open.

## Outcome and evidence limits

Official documentation supplies candidate automatic-setup paths for both
providers. Recommend evaluating Android Google Identity authorization for
YouTube and a public-client device authorization flow for Twitch. These are
engineering proposals, not owner-selected technologies or verified account
integrations. A backend is not established as necessary by this research;
its absence is also not proven on the selected devices/accounts.

The owner's request to implement the recommended next step is applied to
M2.3's documentation research. No application, account registration, consent
grant, provider resource, or broadcast was created. G1 has not been opened by
this documentation task. All scenarios below remain unrun, and T03/T06 remain
proposed test boundaries. Provider rules were checked on the research date;
recheck them before registration and implementation.

| Path | Documentation supports | Evidence still absent |
| --- | --- | --- |
| YouTube automatic | Android authorization, channel lookup, broadcast/stream creation or selection, binding, ingest retrieval, lifecycle transitions | Project/client configuration, actual channel eligibility, grant/account selection, token reacquisition, real setup/ingest |
| Twitch automatic | Public device grant, user-authorized stream-key retrieval, unauthenticated ingest discovery | Public registration, suitability on Android, actual scopes/key access, refresh rotation, real ingest and TLS compatibility |
| Manual | Phone-side URL/key configuration independent of in-app provider authorization | Chosen publisher compatibility, actual destination eligibility and received broadcast |

## YouTube prerequisites and authorization proposal

1. Prepare a Google Cloud project and enable YouTube Data API v3. Channel-owner
   OAuth authorization is required for user operations; an API key alone is not
   account authorization. [Live API prerequisites](https://developers.google.com/youtube/data/live/getting-started).
2. Establish application identity and signing before registering Android OAuth
   clients: package name and distribution certificate SHA-1, branding, and
   consent configuration. Evaluate `AuthorizationClient` for API access;
   Google distinguishes it from Credential Manager authentication. This does
   not add a mandatory LiveStream account or require a backend web client for
   the proposed on-device path. Google's separate offline/server path would
   require its own server design. [Android authorization guide](https://developer.android.com/identity/authorization).
3. Evaluate the single `https://www.googleapis.com/auth/youtube.force-ssl` scope
   for the required writes. Broadcast creation accepts that scope or `youtube`;
   `youtube.readonly` cannot cover creation. Review the complete endpoint set
   and permission presentation before selecting D06's scope; do not request
   both broad scopes by default. [Broadcast creation authorization](https://developers.google.com/youtube/data/live/reference/rest/v3/liveBroadcasts/insert).
4. The channel must be verified and eligible for live streaming, including no
   live-stream restrictions in the prior 90 days and the current minimum age
   of 16. Initial enablement may take up to 24 hours. Use encoder-path evidence
   for this app; do not infer eligibility from the separate YouTube mobile-app
   workflow. Actual owner-channel status is unknown.
   [General eligibility](https://support.google.com/youtube/answer/2474026?hl=en),
   [encoder enablement](https://support.google.com/youtube/answer/2907883?hl=en).

For continued on-device API access, Google describes calling authorization
again after prior consent; its access tokens last about one hour. This is
different from holding a Google refresh token in the APK. Recommend evaluating
SDK-managed token reacquisition without a custom backend; interactive recovery
must remain possible when consent/account state changes.
[Ongoing Android authorization](https://developer.android.com/identity/authorization#maintain-ongoing-access-to-the-users-data).

The API reference provides `clearToken` for cached access-token invalidation
and `revokeAccess` for withdrawing the grant. It also describes account/consent
resolution and unavailable Play services. Proposed handling: check granted
scopes, discard an invalid cached token, and reacquire once before requesting
operator action; never loop account dialogs during a broadcast. Validate the
actual account-switch interaction. [AuthorizationClient reference](https://developers.google.com/android/reference/com/google/android/gms/auth/api/identity/AuthorizationClient).

## YouTube authorization-to-ready workflow

These steps are a proposed application workflow, not executed requests.

| Step | Proposed operation and observable result | Official reference |
| --- | --- | --- |
| Y01 | On Connect YouTube, authorize the selected Google account for the decided scope. Cancellation leaves setup unready and does not publish. | [AuthorizationClient](https://developers.google.com/android/reference/com/google/android/gms/auth/api/identity/AuthorizationClient) |
| Y02 | Call `channels.list(part=snippet,mine=true)` and display the authorized channel identity. Handle an empty result and verify Brand Account/channel selection rather than assuming the Google account identifies every managed channel. | [Channels list](https://developers.google.com/youtube/data/reference/rest/v3/channels/list) |
| Y03 | Offer eligible existing broadcasts using the supported list filters, or create one after reviewing title, scheduled start, visibility, and audience declaration. Keep visibility/audience defaults open; never silently select public. | [Broadcast list](https://developers.google.com/youtube/data/live/reference/rest/v3/liveBroadcasts/list), [creation fields](https://developers.google.com/youtube/data/live/reference/rest/v3/liveBroadcasts/insert) |
| Y04 | Select a compatible existing stream or create a stream with title, ingestion type, resolution, and frame rate. The 720p/30 fps PRD baseline is a feasibility case; D07 still determines adaptive settings. | [Stream creation](https://developers.google.com/youtube/data/live/reference/rest/v3/liveStreams/insert) |
| Y05 | Check the chosen broadcast's binding; bind its ID to the selected stream ID when appropriate. Do not replace an active/incompatible binding without a reviewed recovery path. | [Broadcast binding](https://developers.google.com/youtube/data/live/reference/rest/v3/liveBroadcasts/bind) |
| Y06 | Obtain the stream's `cdn.ingestionInfo.rtmpsIngestionAddress` and `streamName`. Resolve them only inside the streaming phone; expose a sanitized ready result. | [Stream resource](https://developers.google.com/youtube/data/live/reference/rest/v3/liveStreams) |
| Y07 | Show channel, broadcast, visibility, and readiness before Start confirmation. Preparing the destination sends no camera/audio. Inspect existing auto-start settings; propose explicit transitions with auto-start disabled where supported. | [Broadcast lifecycle](https://developers.google.com/youtube/data/live/life-of-a-broadcast) |

After confirmed Start and phone capture readiness, the proposed publisher sends
to the returned RTMPS endpoint. YouTube documents TLS ingestion on port 443;
do not construct an endpoint by changing an arbitrary RTMP URL's scheme.
[RTMPS guide](https://developers.google.com/youtube/data/live/guides/rtmps-ingestion).

Check the bound stream is `active` before transitioning the broadcast to
`testing` or `live`; testing additionally requires a monitor stream. Keep a
transition-in-progress status until the provider reports the target state.
Confirmed Stop stops local publishing even if the API fails, then attempts
the appropriate `complete` transition and reports any unresolved provider
state. Proposed retry/reconciliation details remain D06/D07 work.
[Transition requirements](https://developers.google.com/youtube/data/live/reference/rest/v3/liveBroadcasts/transition).

## Google consent, quotas, and deployment dependencies

| Subject | Finding and planning consequence |
| --- | --- |
| Testing audience | External Testing permits up to 100 listed test users; consent for scopes beyond basic identity expires after seven days, including any issued offline refresh token. This is separate from the approximately one-hour access-token lifetime. Record the actual audience configuration and reconsent behavior. [Audience management](https://support.google.com/cloud/answer/15549945?hl=en). |
| Personal-use verification | Google documents a personal-use exception to mandatory OAuth verification below 100 users, with unverified warnings/limits. Evaluate it for this personal project; it does not establish channel eligibility, waive policies, or choose production/distribution settings. [Verification exceptions](https://support.google.com/cloud/answer/13464323?hl=en). |
| API quota | The current calculator, updated 2026-10-15, lists 10,000 daily units shared by endpoints other than the separate search/upload buckets. The listed live-broadcast bind/insert/list/transition/update and live-stream insert/list operations cost one unit each. Use the actual project's allocation and count polling, pagination, retries, and failures; do not carry forward historical 50-unit write assumptions. [Quota calculator](https://developers.google.com/youtube/data/determine_quota_cost). |
| Audit/review | Extra quota requires a YouTube compliance audit; periodic audits can also apply. OAuth consent verification and YouTube API compliance review are distinct. Record actual requests/outcomes rather than assuming either review is already complete. [Quota and compliance audits](https://developers.google.com/youtube/data/guides/quota_and_compliance_audits). |
| Signing and updates | D12 must connect each installed build to the registered Android client and phone/watch signing plan. Debug authorization success cannot establish release-build authorization. Actual identifiers and distribution choice are pending. |

## Twitch prerequisites and authorization proposal

Register a distinct Twitch application using an account with verified email
and two-factor authentication. Record its category, required console fields,
and **Public** client setting before any probe. The public client ID is an
application identifier; a confidential secret must never be embedded in the
APK. The console's actual configuration is unknown.
[Registration instructions](https://dev.twitch.tv/docs/authentication/register-app/).

Propose Public device authorization without a client secret. Such clients are
limited to the device flow. The guide targets limited-input devices; suitability
and browser return on Android remain unverified. Use returned token expiry
instead of hardcoding it.
[Device grant](https://dev.twitch.tv/docs/authentication/getting-tokens-oauth/#device-code-grant-flow).

## Twitch authorization-to-ready workflow

| Step | Proposed operation and observable result | Official reference |
| --- | --- | --- |
| W01 | POST to `https://id.twitch.tv/oauth2/device` with client ID and `scopes=channel:read:stream_key`; present the returned verification URI/code in the external browser. Poll `/oauth2/token` with the device grant, client ID, code, and requested scopes. Stop on cancellation/expiry. | [Device grant](https://dev.twitch.tv/docs/authentication/getting-tokens-oauth/#device-code-grant-flow) |
| W02 | Respect the returned polling interval; reduce polling for network failures/slow-down, and distinguish pending consent from denial. Test Twitch's actual response shape rather than assuming every error follows the RFC JSON shape. | [RFC 8628 section 3.5](https://datatracker.ietf.org/doc/html/rfc8628#section-3.5) |
| W03 | Validate the resulting token, verify client ID and required scope, and use the returned user ID/login as the broadcaster identity. Validate on startup and at least hourly while maintaining the OAuth session. | [Token validation](https://dev.twitch.tv/docs/authentication/validate-tokens/) |
| W04 | GET `https://api.twitch.tv/helix/streams/key?broadcaster_id=...` with the user bearer token and matching Client-Id. The broadcaster ID must equal the token's user ID. A 403 may indicate additional streaming prerequisites; display a sanitized next step. | [Get Stream Key](https://dev.twitch.tv/docs/api/reference/#get-stream-key) |
| W05 | GET `https://ingest.twitch.tv/ingests`, which requires no authentication. Select a returned ingest template and resolve its stream-key placeholder on the phone. Ingest selection policy remains proposed. | [Ingest discovery](https://dev.twitch.tv/docs/video-broadcast/reference/#get-ingest-servers) |
| W06 | Display the authorized channel and destination as ready. Only confirmed Start initiates publishing. Unlike YouTube, this path does not create/bind a YouTube-style broadcast resource. | [Broadcast guide](https://dev.twitch.tv/docs/video-broadcast/) |

Twitch's broadcast/discovery references describe RTMP templates. This research
does not establish RTMPS support for the selected endpoint/library; leave
endpoint security and any transport fallback as an explicit D03/D06 validation
item. Never silently rewrite the returned template or present RTMP as encrypted.
The guide's bandwidth-test option suppresses live viewing; it cannot pass the
PRD's real received-broadcast acceptance.
[Broadcast URL behavior](https://dev.twitch.tv/docs/video-broadcast/).

### Twitch renewal, disconnect, and documentation discrepancies

The device-flow guide describes single-use rotating refresh tokens and a
30-day inactivity expiry. The general refresh guide instead describes public
refresh tokens expiring 30 days after generation and initially says only
authorization-code tokens can refresh. Its parameter table nevertheless
explicitly exempts public clients from supplying a secret. These statements
do not fully agree. Treat exact refresh lifetime and Android flow behavior as
**unverified**, not as an application timing guarantee.
[Device-flow rules](https://dev.twitch.tv/docs/authentication/getting-tokens-oauth/#device-code-grant-flow),
[refresh guide](https://dev.twitch.tv/docs/authentication/refresh-tokens/).

Propose the documented public refresh request (`client_id`, `grant_type`, and
form-encoded `refresh_token`, without a secret). Serialize refresh, atomically
replace the returned token pair, and recover through consent if the result is
lost or rejected. Do not retry a consumed refresh token indefinitely. Observe
the returned grants/expiry and test invalidation; obtain provider clarification
if the documented public path fails.
[Refresh parameters and concurrency](https://dev.twitch.tv/docs/authentication/refresh-tokens/).

Invalid token validation ends the OAuth session; invalidate cached readiness
and require recovery for future API operations. Provider validation does not
by itself prove that an existing RTMP connection has stopped.
[Validation expectations](https://dev.twitch.tv/docs/authentication/validate-tokens/).
For Disconnect, attempt access-token revocation using client ID and token,
delete local authorization/publishing secrets, and report incomplete remote
revocation truthfully. The endpoint documents access-token revocation; do not
claim that it proves every refresh token or ingest key was invalidated.
[Revocation endpoint](https://dev.twitch.tv/docs/authentication/revoke-tokens/).

Read rate-limit headers and wait for the applicable reset after a general
Helix 429; inspect endpoint-specific errors instead of assuming every 429 has
the same cause. Polling the authorization service has separate flow rules.
[Twitch rate limits](https://dev.twitch.tv/docs/api/guide/#twitch-rate-limits).

## Manual setup and common behavior proposals

Manual mode accepts URL/key on the streaming phone without in-app sign-in.
It preserves all first-release automatic-setup requirements and cannot bypass
channel restrictions. It has no OAuth authority to create/complete a YouTube
event: provider-side broadcast setup and any auto-start/stop settings must be
explained to the operator. Manual Twitch uses an eligible broadcaster's ingest
endpoint/key. [YouTube encoder workflow](https://support.google.com/youtube/answer/2907883?hl=en),
[Twitch manual key/URL path](https://dev.twitch.tv/docs/video-broadcast/).

Proposed adapter results contain account/channel labels, selected event and
visibility when applicable, sanitized endpoint identity, readiness/error, and
a phone-only secret reference. Controllers receive no stream keys, bearer
tokens, device codes, refresh tokens, or credential-bearing URLs. Mask manual
keys and redact raw provider responses before diagnostics; use the PRD's
platform-protected storage requirement. Never collect provider passwords.

Canceled consent or Start confirmation sends no media. Authorization success
alone never means live. Expired authorization, missing scope, eligibility,
quota, and network failures have distinct actionable results. For a lost
response to a resource-creating write, reconcile existing resources/retained
IDs before repeating it; do not create duplicate broadcasts automatically.
Match state survives every provider failure. These are implementation
proposals/PRD applications, not provider guarantees or delivered behaviors.

## Open D06 and D12 dispositions

Every row remains open. No new product requirement or owner decision is inferred.

| Record portion | Recommended direction | Remaining decision/evidence |
| --- | --- | --- |
| D06 — YouTube authorization | Evaluate Android authorization and SDK token reacquisition, without a backend initially | Scope/grant review, account and Brand Account selection, installed Play services, invalid-token and long-session recovery |
| D06 — YouTube event policy | Review event/visibility/audience explicitly; evaluate manual lifecycle transitions | Defaults, reusable stream/broadcast handling, existing auto-start settings, retry/partial-failure cleanup, confirmed Stop reconciliation |
| D06 — Twitch authorization | Evaluate a Public device grant and rotating refresh tokens | Actual public registration, Android suitability, secret-free refresh observations, lifetime discrepancy/provider clarification |
| D06 — Active session changes | Prevent a channel change from silently redirecting an active broadcast | Decide whether in-app switch/disconnect first requires confirmed Stop or is deferred; define externally revoked authorization recovery and reporting |
| D06 — Endpoint compatibility | Prefer verified secure ingest when available | YouTube RTMPS and Twitch endpoint/library evidence; manual RTMP/RTMPS compatibility and disclosed fallback policy |
| D12 — Provider deployment | Align project/client registration with package/signing and personal distribution | Existing registrations, owner account eligibility, consent audience, review/audit status, actual quotas, update/signature combinations |

Backend use, if required by findings, needs an explicit architecture/scope
decision. The documented public mechanisms do not authorize creating one.
The PRD already retains provider-flow/registration and distribution choices as
open in [section 5.4](../../PRD.md#54-details-to-confirm-before-implementation).

## Validation scenarios and retained evidence

All rows are **Pending — not run**. G1 precedes application probes and tests;
confirm T03/T06 before their first repeatable behavioral tests. Actual account
configuration and external changes are separate actions from this research.

| ID | Scenario | Required observable outcome / evidence |
| --- | --- | --- |
| P01 | Inspect the intended Google and Twitch configurations | Dated nonsecret record of API enabled, audience/client type, signing match, scopes, actual quota/review status, and channel readiness; explicitly unknown fields retained |
| P02 | YouTube grant, cancellation, and insufficient scope | Correct selected channel; no setup/publishing on cancel; useful missing-scope recovery; no key copying |
| P03 | YouTube create and select existing event | Both paths identify the intended broadcast, compatible stream/binding, returned ingest details, reviewed visibility/audience, and ready state without camera/audio transmission |
| P04 | YouTube token expiry during a 90–120-minute session | Exercise an API operation after the original token expires; verify SDK reacquisition or visible recovery, retained match, truthful broadcast state, and confirmed Stop handling |
| P05 | YouTube confirmed Start/Stop and failed transition | Provider state checked separately from publisher state; canceled Start publishes nothing; failed completion is visible; local Stop always remains effective |
| P06 | Twitch Public device flow on the selected Android phone | Browser consent, polling/expiry/cancel behavior, exact granted scope and token identity, and ready state without an embedded secret or manual key retrieval |
| P07 | Twitch refresh and interrupted rotation | Real secret-free refresh and replacement; concurrent demand causes one refresh; lost response/restart/reused or invalid refresh token leads to bounded recovery, not credential reuse |
| P08 | Twitch invalidation and scheduled validation | Startup/hourly validation, revoked/expired/mismatched-client/missing-scope results, and no reuse of invalid readiness; ingest effects recorded separately |
| P09 | Eligibility, quotas, and partial setup failures | YouTube live-disabled and quota cases; Twitch stream-key 403 and Helix 429; actionable sanitized results, preserved match, reconciled resources, and no uncontrolled retries |
| P10 | Account switch, disconnect, and reconnect | Apply the decided active-stream policy; delete cached secrets; record local deletion separately from successful provider revocation; restore only the newly authorized identity |
| P11 | Manual setup without sign-in | Real ingest to each provider, correct scoreboard/audio/video, confirmed controls, masked keys, and documented provider-side setup; manual success does not pass automatic setup |
| P12 | Each automatic path through real destination playback | No manual URL/key retrieval; actual device/build, channel/event, endpoint transport, received overlay/audio/video, and start/stop outcomes; a bandwidth-only test is insufficient |

Retain scenario ID, date/time/timezone, physical device/build, application
build/signing label, provider mode, nonsecret configuration, expected/observed
outcome, and sanitized evidence reference. Use private local labels for account
identity in retained reports; no account emails, passwords, tokens, codes,
keys, or complete publishing URLs. Observed expiry/rotation metadata is useful;
the credential value is not. No scenario currently supplies account or broadcast
evidence.

## Completion and delivery handoff

The task's three documentation checklist items are satisfied by the separate
workflows, explicit prerequisites, secret-free proposals, deliberate Start,
and retained manual setup. Overall M2.3 stays **In progress** under the master
plan's feasibility/evidence requirements: real registrations/accounts, the
Twitch discrepancies, and D06/D12 dispositions remain pending. Completing the
research does not satisfy M2.7's M2.3 prerequisite or G2.

Use P01–P10 in the authorized feasibility work and future M5 authorization
tests; P11/P12 feed M4.4, M5.1/M5.2, V05, and V09. Full-duration reliability
and received-overlay acceptance still belong to V01/V02. The application
stack, test tools, provider registrations, and account changes remain unselected
or unperformed as applicable.

## Documentation validation

Validated on 2026-10-20 with the existing planning-package checker and the
pre-work PRD hash:

```powershell
node plan/tools/check-planning-package.cjs ed4210adad0f95a062ae557c4b56528f93aa55f109e67965b73c4f408336db58
```

Result: **PASS**, exit code 0, no errors. The check covered 68 Markdown
documents, 1,065 local link occurrences, 241 heading links, 53 task files/index
rows, 159 dependency edges, eight Mermaid pairs, and nine locked skills. The
PRD matched its pre-work hash. AGENTS.md and skills-lock.json also matched
their pre-work hashes.

A separate reconciliation passed for seven completed tasks, one in progress,
45 not started, all nine milestone totals, and 13 open decision records.
Y01–Y07, W01–W06, and P01–P12 are unique and sequential. The checked M2.3
criteria describe completed documentation; pending feasibility is explicitly
retained in the task, index, master plan, and dashboard. Official references
were reviewed for the distinct scope, resource, renewal, and confirmation
paths; unresolved provider statements are identified above.

These are documentation checks. No application test, account authorization,
physical-device, encoder, or received-broadcast result was produced.

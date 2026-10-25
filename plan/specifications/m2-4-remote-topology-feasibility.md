# M2.4 controller topology investigation

Recorded: 2026-10-23 (America/New_York)  
Status: Protocol/topology proposal prepared; radio and network validation pending.  
Task: [M2.4](../subplans/m2-4-remote-topology-feasibility.md)

## Proposed topology

Evaluate an authenticated LAN connection between the second Android device and
the streaming phone, using a local TLS WebSocket or equivalent ordered channel.
Pair by a locally reviewed QR/bootstrap secret, pin the streamer's session
identity, authorize capabilities on the phone, and support revocation. Keep
destination tokens/keys entirely outside controller payloads. Transport pairing
never grants unrestricted application authority.

Keep Q7: the watch is paired directly to the active streamer and uses Data Layer.
Phone/watch package names and signatures must match, and Data Layer may use
Bluetooth or Google-mediated network routing. It is not a Bluetooth-only
guarantee. [Official Data Layer overview](https://developer.android.com/training/wearables/data/overview).
No relay through another companion phone is proposed.

| Option | Proposed disposition | Evidence missing |
| --- | --- | --- |
| Shared Wi-Fi LAN, cellular publisher | First topology to compare when LAN connectivity exists. | Client isolation, runtime LAN permission, reachability and actual outgoing cellular route. |
| Streamer hotspot, controller as client | Compare for venues without a usable LAN. | Tethering/uplink coexistence, power and reconnect behavior on selected builds. |
| Bluetooth Android-phone transport | Keep as an alternative if LAN/hotspot is unsuitable. | Pairing UX, implementation effort and measured reliability. |
| Watch Data Layer | Confirmed connection requirement; application protocol still proposed. | Direct physical pairing, signing and actual command/status exchange. |

Android 17 adds runtime local-network access restrictions for target API 37;
discovery/connection and permission denial must be evaluated together.
[Android 17 behavior changes](https://developer.android.com/about/versions/17/behavior-changes-17#local-network-permission).
Android's Network API supports network-bound sockets. The proposed publisher
must expose a compatible injection point; a generic networking API does not
prove the chosen RTMP library can use it.
[Network socket factory](https://developer.android.com/reference/android/net/Network#getSocketFactory()).
Bind the intended publishing connection, not the entire process blindly,
because controllers may need a simultaneous Wi-Fi path.

## Protocol contract and physical cases

Propose stable match/team identities, command IDs, an expected revision,
capabilities, explicit durable results and authoritative snapshots. Serialize
accepted local/Android/watch edits at the phone. Retries reuse the command ID;
reconnect requires a fresh snapshot before new edits. A lost acknowledgment
cannot become a second point. Exact receipt expiry and retention remain D11.
Q45's accepted numeric edit and resulting stream stop need separate results.

All cases are unrun: RT01 authorized pair/revoke; RT02 permission denial/client
isolation; RT03 concurrent score/Undo and lost acknowledgment; RT04 disconnect
and fresh-snapshot recovery; RT05 direct-watch signing/companion mismatch;
RT06 simultaneous cellular publishing and Wi-Fi/hotspot control; RT07 switch
streaming-phone roles and repeat direct pairing. Record device/build, topology,
actual route, expected/observed state and sanitized timing separately.

The owner has no devices available now, so this is an explicit physical blocker.
No topology is demonstrated; M2.4/D05/D09 remain open.

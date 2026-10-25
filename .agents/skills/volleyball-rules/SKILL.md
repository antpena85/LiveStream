---
name: volleyball-rules
description: Indoor volleyball match state machine, scoring rules (FIVB 2025-2028), sideout serving logic, timeout management, and broadcast scoreboard overlay layout standards.
---

# Volleyball Rules & Scoreboard State Management

## Overview

This skill provides domain rules, state machine specifications, and broadcast overlay guidelines for indoor volleyball live streaming. Use this skill when designing, implementing, or validating match state transitions, remote scoring controls, and video scoreboard overlays.

---

## 1. Indoor Volleyball Scoring Rules (FIVB 2025-2028 Standard)

### Match Formats

1. **Standard Match (Best of 5):**
   - First team to win **3 sets** wins the match.
   - Sets 1–4: Played to **25 points**, rally scoring, must win by at least **2 points** (no point cap).
   - Set 5 (Deciding Set): Played to **15 points**, rally scoring, must win by at least **2 points** (no point cap).
2. **Short Match Variant (Best of 3):**
   - First team to win **2 sets** wins the match.
   - Sets 1–2: Played to **25 points**, rally scoring, must win by at least **2 points** (no point cap).
   - Set 3 (Deciding Set): Played to **15 points**, rally scoring, must win by at least **2 points** (no point cap).
3. **Custom Preset Options:**
   - User-configurable target points per regular set, deciding set target, winning margin, sets required to win, and optional hard point cap.

### Timeouts & Intervals

- **Team Timeouts:** Each team is allowed a maximum of **2 timeouts per set**.
- **Timeout Reset:** The timeout count resets to the full allowance at the start of each new set.
- **Unused Timeouts:** Unused timeouts do not carry over to subsequent sets.

### Court Side Changes

- **Between Sets:** Teams change courts after each set (except before the deciding set, which requires a new coin toss/selection).
- **Deciding Set Court Switch:** In the deciding set (Set 5 in best-of-5, or Set 3 in best-of-3), teams change courts as soon as the leading team reaches **8 points**. Match values, scores, and serving order remain unchanged.

---

## 2. Match State Machine

### State Data Model

```
MatchState:
  teamA:
    name: String (e.g. "Eagles")
    shortName: String (e.g. "EAG")
    color: Color (optional hex/accent)
    setsWon: Int (0..3)
    currentPoints: Int (0..N)
    timeoutsRemaining: Int (0..2)
  teamB:
    name: String (e.g. "Panthers")
    shortName: String (e.g. "PAN")
    color: Color (optional hex/accent)
    setsWon: Int (0..3)
    currentPoints: Int (0..N)
    timeoutsRemaining: Int (0..2)
  currentSetNumber: Int (1..5)
  servingTeam: Enum (TEAM_A, TEAM_B, NONE)
  displayOrderSwapped: Boolean (false = Team A left, Team B right; true = Team B left, Team A right)
  setHistory: List of SetResult (e.g. [{set: 1, scoreA: 25, scoreB: 22}, ...])
  isSetPoint: Boolean
  isMatchPoint: Boolean
  isMatchComplete: Boolean
```

### State Transitions & Serving Possession (Sideout Logic)

1. **Rally Scoring & Sideout Serve Transition:**
   - If the **serving team** wins the rally:
     - Point is incremented for the serving team.
     - Serve possession remains with that team.
   - If the **receiving team** wins the rally (Sideout):
     - Point is incremented for the receiving team.
     - Serve possession automatically toggles to the receiving team.
   - *Manual Override:* Controllers must allow the scorekeeper to manually toggle serving possession if an error occurred or before the first serve of a set.
2. **Point Subtraction & Undo:**
   - Scorekeepers can decrement a point or trigger "Undo".
   - Undo must restore the exact previous state (points, serving possession, and timeout count).
   - Points cannot become negative (< 0).
3. **Set Completion Transition:**
   - A set is won when a team reaches the target points (25, or 15 in deciding set) with a lead of >= 2 points.
   - The UI indicates **Set Point** or **Match Point**.
   - Advancing the set requires **explicit user confirmation** (not automated) to avoid premature advancement during disputed calls.
   - Upon confirmation:
     - Increment `setsWon` for the winning team.
     - Record the completed score in `setHistory`.
     - Reset `currentPoints` for both teams to 0.
     - Reset `timeoutsRemaining` for both teams to 2.
     - Increment `currentSetNumber`.
4. **Court Side Swap:**
   - Triggering "Swap Sides" toggles `displayOrderSwapped`.
   - Swaps visual positions of Team A and Team B on the broadcast overlay so they match what the camera sees on court.
   - Preserves all team scores, sets won, timeouts, and serve possession without alteration.

---

## 3. Broadcast Scoreboard Overlay Guidelines

### Resolution & Layout Specs

- **Target Output Resolution:** 1280x720 (720p, 16:9) baseline; scalable to 1920x1080 (1080p).
- **Safe Margins:** Keep all graphics within a 5% title safe margin from screen edges (min. 36px at 720p, 54px at 1080p).
- **Recommended Placement:** Top-left banner or bottom-center bug.

### Scoreboard Elements (Compact Layout)

```
+-------------------------------------------------------------+
| [•] TEAM A   25 | (2)   SETS   (1) | 22   TEAM B        | |
|     T/O: ●●     |      SET 3       |      T/O: ●○       | |
+-------------------------------------------------------------+
```

1. **Team Identifier:** Team name or short code (e.g. 3–5 characters), truncated gracefully if too long.
2. **Current Set Points:** Prominent, high-contrast numeric display (largest visual element).
3. **Serving Indicator (`[•]`):** Distinct visual pip, dot, or icon adjacent to the team currently serving.
4. **Sets-Won Counter:** Compact digits (e.g., `2 - 1`) or dots representing sets won.
5. **Timeouts Indicator:** Dots or bars representing timeouts remaining (e.g. `●●` = 2 left, `●○` = 1 left, `○○` = 0 left).
6. **Set Number Indicator:** Small label indicating current set (e.g., `SET 3`).

### Legibility & Visual Contrast Standards

- Use bold, sans-serif typography for scores and team names.
- Provide a semi-transparent dark background (e.g. 75–85% black `#000000D9`) or contrasting badge behind text to maintain legibility over varying gymnasium floor colors and lighting.
- Ensure minimum contrast ratio of **4.5:1** (WCAG AA).

---

## 4. Remote Controller Operational Patterns

- **Streaming Phone:** Full control, video preview, audio level meter, direct Wear OS communication.
- **Second Phone/Tablet:** Full scoring control (points, serve toggle, timeouts, set confirm, side swap, team names). Communicates over local network / Wi-Fi Hotspot.
- **Wear OS Watch:** Quick-action wrist interface:
  - Big touch targets for: `+ Point Left`, `+ Point Right`, `Undo`.
  - Secondary screen/long-press for: `Timeout`, `Swap Sides`, `Confirm Set`.
  - Display current score, sets, and serve possession at a glance.


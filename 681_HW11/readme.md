# Gmail Email System - Race Condition and Thread Safety

## Overview

This project simulates a Gmail-like email system where multiple threads perform concurrent operations:
1. **Reading emails** - Marks emails as read and updates the unread count.
2. **Deleting emails** - Removes emails from the inbox and adjusts the unread count if necessary.

The project demonstrates:
- **Race conditions** in a thread-unsafe implementation.
- A **thread-safe implementation** that fixes the issues using `ReentrantLock`.
- A clean **2-step thread termination** to gracefully stop the threads.

---

## Features

- **Thread-Unsafe Version**:
  - Demonstrates a race condition when multiple threads access and modify shared resources (e.g., `unreadCount` and the inbox list).

- **Thread-Safe Version**:
  - Implements `ReentrantLock` to synchronize critical sections, ensuring thread safety.
  - Uses a **2-step thread termination**:
    1. **Stop signal** using a `volatile boolean` flag.
    2. **Graceful termination** by allowing threads to clean up and finish their work.





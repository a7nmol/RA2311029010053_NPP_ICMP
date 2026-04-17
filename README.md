# RA2311029010053_NPP_ICMP
Anmol Bharat
RA2311029010053
Section: V2
B.Tech CSE (Computer Networking)

# 🌐 ICMP TTL Simulator (Java GUI)

## 📌 Overview

This project is a Java-based GUI application that simulates how packets travel across a network using **Time To Live (TTL)** and demonstrates how the **ICMP (Internet Control Message Protocol)** works when errors occur.

It visually represents packet movement across multiple routers and shows what happens when a packet successfully reaches its destination or gets dropped due to TTL expiration.

---

## 🎯 Objective

The main goal of this project is to understand:

* How TTL works in packet forwarding
* How routers process packets at each hop
* How ICMP generates error messages like *Time Exceeded*
* Basic networking concepts through visualization

---

## ⚙️ Features

* GUI-based simulation using Java Swing
* Input fields for Source IP, Destination IP, and TTL
* Step-by-step packet traversal across routers
* Real-time log display of packet activity
* Visual representation of routers and connections
* Detection of:

  * Successful packet delivery
  * TTL expiration (ICMP Type 11 – Time Exceeded)
  * Destination unreachable scenarios

---

## 🧠 How It Works

1. User enters Source IP, Destination IP, and TTL value
2. Packet starts traveling through predefined routers
3. At each hop:

   * TTL is decremented
   * Router processes the packet
4. If TTL becomes 0:

   * Packet is dropped
   * ICMP Time Exceeded message is generated
5. If destination is reached:

   * Packet is successfully delivered

---

## 🖥️ Technologies Used

* Java
* Java Swing (GUI)
* AWT (Graphics & Layouts)
* Regular Expressions (for IP validation)
* Multithreading (for smooth simulation)

---

## 📊 Key Concepts Demonstrated

* ICMP Protocol (Error handling & diagnostics)
* TTL (Time To Live) mechanism
* Packet switching and routing
* Network visualization

---

## 🚀 How to Run

1. Clone the repository
2. Open the project in any Java IDE (VS Code / IntelliJ / Eclipse)
3. Compile and run the main class:

   ```
   TTLSimulatorGUI.java
   ```
4. Enter input values and click **Start Simulation**

---

## 📷 Output

* Visual network showing routers
* Packet movement across hops
* Logs displaying TTL changes and ICMP messages

---

## 📌 Future Improvements

* Add dynamic routing paths
* Implement more ICMP types (Echo Request/Reply)
* Add real-time animation speed control
* Enhance UI design

---


## 📄 License

This project is for educational purposes only.


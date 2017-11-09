# DS-CIS656-Lab3

### Project structure 

Following is the project structure of the files that matters for this project and a brief explanation of their content.

```bash
├── LogicalCLocks
│   ├── src
│   │   ├── edu
│   │   │   ├── gvsu
│   │   │   │   ├── cis
│   │   │   │   │   ├── cis656
│   │   │   │   │   │   ├── clock
│   │   │   │   │   │   │   ├── Clock.java
│   │   │   │   │   │   │   ├── VectorClock.java
│   │   │   │   │   │   │   ├── VectorClockComparator.java
│   │   │   │   │   │   ├── message
│   │   │   │   │   │   │   ├── ErrorCodes.java
│   │   │   │   │   │   │   ├── Message.java
│   │   │   │   │   │   │   ├── MessageComparator.java
│   │   │   │   │   │   │   ├── MessageTypes.java
│   │   │   │   │   │   ├── queue
│   │   │   │   │   │   │   ├── PriorityQueue.java
│   │   │   │   │   │   ├── ChatClient.java
│   │   │   │   │   │   ├── Main.java
│   │   │   │   │   │   ├── MessageListener.java
```

* Clock.java: Provided interface.
* **VectorClock.java: Class implementing Clock interface. ALl needed methods are implemented and running as expected.**
* **VectorClockComparator.java: Implemented method inside this class that validates between two clocks which one happened first.**
* ErrorCodes.java: Provided error logs class.
* Message.java: Provided Message class.
* **MessageComparator.java: Implemented method inside this class that uses VectorClockComparator's method to compare two clocks**
* MessageTypes.java: Provided class with constant types.
* PriorityQueue.java: Provided class to handle priorities on messages.
* **Main.java: Main class wwhich starts the app. It verifies correct initialization of the app and starts a ChatClient**
* **ChatClient.java: The clientChat class on which all the logic is executed. It registers the user and allows it to send messages and receive messages in a proper way (in the expected order)**
* **MessageListener.java: Thread started from ChatClient class that is always looking for incoming messages and sends them to the ChatCLient that initiated it**


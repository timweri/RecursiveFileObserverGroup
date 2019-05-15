# RecursiveFileObserverGroup
Monitors multiple files/directories recursively on Android

## Summary
This module is necessitated by the fact that the `FileObserver` module provided by Android library (before Android Q) does not support tracking inner files or multiple paths in the same instance of `FileObserver`.

This module creates an abstraction layer that allows one instance of the `RecursiveFileObserverGroup` to watch multiple files and directories recursively.

## Structure
All of the code is concisely contained within 3 files in [`app/src/main/java/io/timweri/recursivefileobservergroup`](app/src/main/java/io/timweri/recursivefileobservergroup):
- `SingleFileObserver.java`: almost identical to `FileObserver` except for it fires the `onEvent` of a parent `SingleRecursiveFileObserver`.
- `SingleRecursiveFileObserver.java`: recursively traverses a given directory and generates multiple `SingleFileObserver`s to watch all of the subdirectories and files in the given directory.
- `RecursiveFileObserverGroup.java`: takes in a list of paths and creates one `SingleRecursiveFileObserver` for each path in the list.

## Usage
```java
List<String> filePaths = new ArrayList<String>;
filePaths.add("/sdcard/Download");
filePaths.add("/sdcard/Whatsapp");
...

RecursiveFileObserverGroup obvGrp = new RecursiveFileObserverGroup(filePaths, FileObserver.ALL_EVENTS);
obvGrp.startWatching();
```

## Credits
The idea and a major part of the implementation are adapted from https://github.com/dlech/owncloud-android.

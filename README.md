# sb1fs

This is a [jnr-fuse](https://github.com/SerCeMan/jnr-fuse) based application for exposing hierarchical bank data as a file system.

## Build project

    $ ./mvnw clean install

## Create local folder for mounting

    $ mkdir tmp

## Mount sb1fs
    
    $ java -jar target/sb1fs-0.0.1-SNAPSHOT.jar --mountPath=tmp
    
## Open another terminal and test away!!!

    $ ls tmp

## Prerequisites
### Linux
Ensure libfuse is installed (package name for RHEL is ``fuse-libs``).

### macOS
Install [Homebrew](https://brew.sh) and then:

    $ brew cask install osxfuse

The very first time [osxfuse](https://osxfuse.github.io/) is used will macOS in some cases block it and you'll have to
manually allow apps from "Benjamin Fleischer" under "Security & Privacy" in "System Preferences" and retry.

N.B. You may [not want to run apps like Spotify](https://github.com/osxfuse/osxfuse/issues/437) when attempting to allow
osxfuse to run.

###  Windows
Notes: You need windows 10 to run winfsp

 * Download and install [winfsp](http://www.secfs.net/winfsp/download/)
 * Run the sb1fs application (default mount point is S:\ for windows)
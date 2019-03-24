# Android threads


## Processes and threads overview
When an application component starts and the application does not have any other components running, the Android system starts a new Linux process for the application with a single thread of execution. By default, all components of the same application run in the same process and thread (called the "main" thread). If an application component starts and there already exists a process for that application (because another component from the application exists), then the component is started within that process and uses the same thread of execution. However, you can arrange for different components in your application to run in separate processes, and you can create additional threads for any process.
Source: https://developer.android.com/guide/components/processes-and-threads.html#Threads

## Background Tasks
Every Android app has a main thread which is in charge of handling UI (including measuring and drawing views), coordinating user interactions, and receiving lifecycle events. If there is too much work happening on this thread, the app appears to hang or slow down, leading to an undesirable user experience. Any long-running computations and operations such as decoding a bitmap, accessing the disk, or performing network requests should be done on a separate background thread. In general, anything that takes more than a few milliseconds should be delegated to a background thread. Some of these tasks may be required to be performed while the user is actively interacting with the app
Source: https://developer.android.com/training/best-background

## Looper, handler and handlerthread
Source: https://blog.mindorks.com/android-core-looper-handler-and-handlerthread-bd54d69fe91a

## Run handlerin a background thread 

Creating a handler from the UI thread will inherit the same thread. To create a handler from the background thread is necessary to create a HandlerThread and get its thread ( .getLooper()) when creating the handler.

Source: https://stackoverflow.com/questions/18694732/run-handler-messages-in-a-background-thread

## Thread annotations:
Thread annotations check if a method is called from a specific type of thread. The following thread annotations are supported:

@MainThread
@UiThread
@WorkerThread
@BinderThread
@AnyThread

Source: https://developer.android.com/studio/write/annotations#thread-annotations 
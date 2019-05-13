package io.timweri.recursivefileobservergroup;

import android.os.FileObserver;

class SingleFileObserver extends FileObserver {
    private String rootPath;
    private final SingleRecursiveFileObserver mSingleRecursiveFileObserver;

    public SingleFileObserver(SingleRecursiveFileObserver recursiveObserver, String path, int mask) {
        super(path, mask);
        mSingleRecursiveFileObserver = recursiveObserver;
        rootPath = path;
    }

    @Override
    public void onEvent(int event, String path) {
        String fullPath = rootPath + '/' + path;
        mSingleRecursiveFileObserver.onEvent(event, fullPath);
    }
}

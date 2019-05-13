package io.timweri.recursivefileobservergroup;

import android.os.FileObserver;

class SingleFileObserver extends FileObserver {
    private String rootPath;
    private final RecursiveFileObserver mRecursiveFileObserver;

    public SingleFileObserver(RecursiveFileObserver recursiveObserver, String path, int mask) {
        super(path, mask);
        mRecursiveFileObserver = recursiveObserver;
        rootPath = path;
    }

    @Override
    public void onEvent(int event, String path) {
        String fullPath = rootPath + '/' + path;
        mRecursiveFileObserver.onEvent(event, fullPath);
    }
}

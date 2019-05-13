package io.timweri.recursivefileobservergroup;

import android.os.FileObserver;

import java.util.ArrayList;
import java.util.List;

public class RecursiveFileObserverGroup extends FileObserver {
    private List<SingleRecursiveFileObserver> mObservers;
    private List<String> rootPaths;
    private final int mMask;

    public RecursiveFileObserverGroup(List<String> paths) {
        this(paths, ALL_EVENTS);
    }

    public RecursiveFileObserverGroup(List<String> paths, int mask) {
        super("", mask);
        rootPaths = paths;
        mMask = mask;
    }

    public void addFilePath(String path) {
        rootPaths.add(path);
    }

    @Override
    public void startWatching() {
        if (mObservers != null) return;
        mObservers = new ArrayList<SingleRecursiveFileObserver>();
        for (String path : rootPaths) {
            mObservers.add(new SingleRecursiveFileObserver(this, path, mMask));
        }
        for (SingleRecursiveFileObserver obv : mObservers) {
            obv.startWatching();
        }
    }

    @Override
    public void stopWatching() {
        if (mObservers == null) return;
        for (SingleRecursiveFileObserver obv : mObservers) {
            obv.stopWatching();
        }
        mObservers.clear();
        mObservers = null;
    }

    @Override
    public void onEvent(int event, String path) {
        // do what you want here
    }
}

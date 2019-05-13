package io.timweri.recursivefileobservergroup;

import android.os.FileObserver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class SingleRecursiveFileObserver extends FileObserver {
    private List<SingleFileObserver> mObservers;
    private String rootPath;
    private final int mMask;
    private final RecursiveFileObserverGroup mObserverGroup;

    public SingleRecursiveFileObserver(RecursiveFileObserverGroup observerGroup, String path) {
        this(observerGroup, path, ALL_EVENTS);
    }

    public SingleRecursiveFileObserver(RecursiveFileObserverGroup observerGroup, String path, int mask) {
        super(path, mask);
        rootPath = path;
        mMask = mask;
        mObserverGroup = observerGroup;
    }

    @Override
    public void startWatching() {
        if (mObservers != null) return;
        mObservers = new ArrayList<SingleFileObserver>();
        Stack<String> pathStack = new Stack<String>();
        pathStack.push(rootPath);

        while (!pathStack.empty()) {
            String parent = pathStack.pop();
            mObservers.add(new SingleFileObserver(this, parent, mMask));
            File path = new File(parent);
            File[] files = path.listFiles();
            if (files == null) continue;
            for (File file : files) {
                if (file.isDirectory() && !file.getName().equals(".") && !file.getName().equals("..")) {
                    pathStack.push(file.getPath());
                }
            }
        }

        for (SingleFileObserver obv : mObservers) {
            obv.startWatching();
        }
    }

    @Override
    public void stopWatching() {
        if (mObservers == null) return;
        for (SingleFileObserver obv : mObservers) {
            obv.stopWatching();
        }
        mObservers.clear();
        mObservers = null;
    }

    @Override
    public void onEvent(int event, String path) {
        String fullPath = rootPath + '/' + path;
        mObserverGroup.onEvent(event, path);
    }
}

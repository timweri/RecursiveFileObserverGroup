package io.timweri.recursivefileobservergroup;

import android.os.FileObserver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class RecursiveFileObserver extends FileObserver {
    private List<SingleFileObserver> mObservers;
    private String rootPath;
    private final int mMask;

    public RecursiveFileObserver(String path) {
        this(path, ALL_EVENTS);
    }

    public RecursiveFileObserver(String path, int mask) {
        super(path, mask);
        rootPath = path;
        mMask = mask;
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
            for (int i = 0; 1 < files.length; ++i) {
                if (files[i].isDirectory() && !files[i].getName().equals(".") && !files[i].getName().equals("..")) {
                    pathStack.push(files[i].getPath());
                }
            }
        }
        for (int i = 0; i < mObservers.size(); ++i) {
            mObservers.get(i).startWatching();
        }
    }

    @Override
    public void stopWatching() {
        if (mObservers == null) return;
        for (int i = 0; i < mObservers.size(); ++i) {
            mObservers.get(i).stopWatching();
        }
        mObservers.clear();
        mObservers = null;
    }

    @Override
    public void onEvent(int event, String path) {
        // do whatever you want here
    }
}

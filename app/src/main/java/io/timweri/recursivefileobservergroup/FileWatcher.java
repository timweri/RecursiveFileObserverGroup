package io.timweri.recursivefileobservergroup;

import android.os.FileObserver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public abstract class FileWatcher {
    private List<SingleRecursiveFileObserver> mObservers;
    private List<String> rootPaths;
    private int mMask = android.os.FileObserver.ALL_EVENTS;

    public FileWatcher(List<String> paths) {
        rootPaths = paths;
    }

    public FileWatcher(List<String> paths, int mask) {
        this(paths);
        mMask = mask;
    }

    public void addPath(String path) {
        rootPaths.add(path);
    }

    public void setPaths(List<String> paths) {
        rootPaths = paths;
    }

    public List<String> getPaths() {
        return rootPaths;
    }

    public void setMask(int mask) {
        mMask = mask;
    }

    public int getMask() {
        return mMask;
    }

    public void startWatching() {
        if (mObservers != null) return;
        mObservers = new ArrayList<>();
        for (String path : rootPaths) {
            mObservers.add(new SingleRecursiveFileObserver(path));
        }
        for (SingleRecursiveFileObserver obv : mObservers) obv.startWatching();
    }

    public void stopWatching() {
        if (mObservers == null) return;
        for (SingleRecursiveFileObserver obv : mObservers) obv.stopWatching();
        mObservers.clear();
        mObservers = null;
    }

    public abstract void onEvent(int event, String path);

    private class SingleRecursiveFileObserver {
        private List<SingleFileObserver> mObservers;
        private String rootPath;

        public void setRootPath(String path) {
            rootPath = path;
        }

        public String getRootPath() {
            return rootPath;
        }

        public SingleRecursiveFileObserver(String path) {
            setRootPath(path);
        }

        public void startWatching() {
            if (mObservers != null) return;
            mObservers = new ArrayList<>();
            Stack<String> pathStack = new Stack<>();
            pathStack.push(rootPath);

            while (!pathStack.empty()) {
                String parent = pathStack.pop();
                mObservers.add(new SingleFileObserver(parent));
                File path = new File(parent);
                File[] files = path.listFiles();
                if (files == null) continue;
                for (File file : files) {
                    if (file.isDirectory() && !file.getName().equals(".") && !file.getName().equals("..")) {
                        pathStack.push(file.getPath());
                    }
                }
            }

            for (SingleFileObserver obvs : mObservers) obvs.startWatching();
        }

        public void stopWatching() {
            if (mObservers == null) return;
            for (SingleFileObserver obv : mObservers) obv.stopWatching();
            mObservers.clear();
            mObservers = null;
        }

        private class SingleFileObserver extends android.os.FileObserver {
            private String rootPath;

            public SingleFileObserver(String path) {
                super(path, mMask);
                rootPath = path;
            }

            @Override
            public void onEvent(int event, String path) {
                String fullPath = rootPath + '/' + path;
                FileWatcher.this.onEvent(event, path);
            }
        }
    }
}

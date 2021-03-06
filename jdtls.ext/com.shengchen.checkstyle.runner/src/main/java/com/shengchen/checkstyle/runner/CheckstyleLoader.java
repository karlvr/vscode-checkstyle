/*
 * Copyright (C) jdneo

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.shengchen.checkstyle.runner;

import com.shengchen.checkstyle.quickfix.QuickFixService;
import com.shengchen.checkstyle.runner.api.ICheckerService;
import com.shengchen.checkstyle.runner.api.IQuickFixService;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CheckstyleLoader {

    static String checkerClass = "com.shengchen.checkstyle.checker.CheckerService";

    URLClassLoader checkerClassLoader = null;

    public ICheckerService loadCheckerService(String checkstyleJarPath, List<String> modulejarPaths) throws Exception {
        if (checkerClassLoader != null) {
            checkerClassLoader.close();
        }
        final ArrayList<URL> jarUrls = new ArrayList<>();
        jarUrls.add(Paths.get(getServerDir(), "com.shengchen.checkstyle.checker.jar").toUri().toURL());
        jarUrls.add(Paths.get(checkstyleJarPath).toUri().toURL());
        for (final String module: modulejarPaths) {
            jarUrls.add(Paths.get(module).toUri().toURL());
        }
        checkerClassLoader = new URLClassLoader(jarUrls.toArray(new URL[0]), getClass().getClassLoader());
        final Constructor<?> constructor = checkerClassLoader.loadClass(checkerClass).getConstructor();
        return (ICheckerService) constructor.newInstance();
    }

    public IQuickFixService loadQuickFixService() {
        return new QuickFixService();
    }

    private String getServerDir() throws Exception {
        final File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getFile());
        return jarFile.getParentFile().getCanonicalPath();
    }
}

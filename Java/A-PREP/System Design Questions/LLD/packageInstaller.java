// Question: Design a package installer system that can install a package
// along with all its dependencies.
// Each package has an "install()" method to install itself.
// Implement a solution that, given a package,
// installs the given package and its dependencies.


import java.util.*;

public class PackageInstaller {
    private final Map<String, List<String>> dependencies; // Adjacency list for dependencies
    private final Set<String> installed; // Tracks installed packages
    private final Set<String> visiting; // Tracks packages in the current recursion stack to detect cycles

    public PackageInstaller(Map<String, List<String>> dependencies) {
        this.dependencies = dependencies;
        this.installed = new HashSet<>();
        this.visiting = new HashSet<>();
    }

    public PackageInstaller() {
        this.dependencies = new HashMap<>();
        this.installed = new HashSet<>();
        this.visiting = new HashSet<>();
    }


    public void installPackage(String pkg) {
        if (installed.contains(pkg)) {
            System.out.println(pkg + " is already installed.");
            return;
        }

        if (visiting.contains(pkg)) {
            throw new IllegalStateException("Cycle detected! " + pkg + " is part of a dependency cycle.");
        }

        System.out.println("Installing " + pkg + "...");
        visiting.add(pkg);

        if (dependencies.containsKey(pkg)) {
            for (String dep : dependencies.get(pkg)) {
                installPackage(dep);
            }
        }

        visiting.remove(pkg);
        installed.add(pkg);
        System.out.println(pkg + " installed successfully.");
    }
}

// Example dependency graph
const dependencies = {
  "nodejs": ["npm", "v8engine"],
  "npm": ["nodejs-core"],
  "v8engine": ["libuv"],
  "react": ["nodejs", "babel"],
  "babel": ["core-js"],
  "typescript": ["nodejs"]
};

const installer = new PackageInstaller(dependencies);

// Installing a package with dependencies
try {
  installer.installPackage("react");
} catch (error) {
  console.error(error.message);
}

// Cycle detection
dependencies["libuv"] = ["nodejs"]; // Creating a cycle

const installerWithCycle = new PackageInstaller(dependencies);
try {
  installerWithCycle.installPackage("react");
} catch (error) {
  console.error( error.message);

}


# Clevis

Clevis is a Java command-line vector graphics application with a lightweight Swing visualizer. It supports creating and manipulating basic shapes, grouping them, querying bounding boxes and intersections, and viewing the current canvas through either the original CLI or the GUI.

Originally developed as an object-oriented programming course project, this version has been cleaned and extended for portfolio demonstration while preserving the original command syntax and CLI workflow.

## Features

- CLI commands for `rectangle`, `square`, `circle`, `line`, `group`, `ungroup`, `delete`, `move`, `boundingbox`, `shapeat`, `intersect`, `list`, `listall`, `undo`, and `redo`
- Swing GUI entry point with command input, output log panel, and live drawing canvas
- Object-oriented shape hierarchy using inheritance and polymorphism
- Composite `Group` shape support
- Command parsing separated through a reusable `CommandProcessor` adapter
- HTML and text command logging
- JUnit 4 integration tests for core behavior and regression coverage

## Tech Stack

- Java 21
- Java SE Swing/AWT
- JUnit 4
- IntelliJ IDEA compatible project layout

## Architecture

The project keeps the model focused on shape state and geometry. Commands are still parsed by the existing `Commander`, while `CommandProcessor` lets both CLI and GUI reuse the same command execution path.

```text
src/hk/edu/polyu/comp/comp2021/clevis/
├── Application.java          # CLI entry point
├── GuiApplication.java       # Swing GUI entry point
├── Logger/                   # command logging
├── controller/               # command processing adapter
└── model/                    # shapes, groups, and shape manager
```

## Screenshots

Screenshot placeholders are kept under:

- `docs/screenshots/gui-demo.png`
- `docs/screenshots/cli-demo.png`

No screenshots are included yet.

## Run From Terminal

From the repository root:

```bash
javac -d out/production/clevis $(find src -name "*.java")
java -cp out/production/clevis hk.edu.polyu.comp.comp2021.clevis.Application
```

Optional log paths:

```bash
java -cp out/production/clevis hk.edu.polyu.comp.comp2021.clevis.Application -html logs.html -txt logs.txt
```

## Run The GUI

Compile first, then launch:

```bash
javac -d out/production/clevis $(find src -name "*.java")
java -cp out/production/clevis hk.edu.polyu.comp.comp2021.clevis.GuiApplication
```

The GUI accepts the same command syntax as the CLI. It draws top-level shapes and recursively draws shapes inside groups.

## CLI Example

Command keywords are case-insensitive; shape names remain case-sensitive.

```text
rectangle r1 10 10 120 80
circle c1 220 120 50
line l1 0 0 300 200
group g1 r1 c1
move g1 20 10
boundingbox g1
listall
quit
```

More examples are available in `examples/sample_commands.txt`.

## Run Tests

```bash
javac -d out/production/clevis $(find src -name "*.java")
javac -cp out/production/clevis:lib/junit-4.12.jar:lib/hamcrest-core-1.3.jar -d out/test/clevis $(find test -name "*.java")
java -cp out/production/clevis:out/test/clevis:lib/junit-4.12.jar:lib/hamcrest-core-1.3.jar org.junit.runner.JUnitCore hk.edu.polyu.comp.comp2021.clevis.model.ClevisTest
```

## Notes And Limits

- `intersect` currently uses axis-aligned bounding box overlap, not exact computational geometry for every shape pair.
- `shapeat` checks whether a point is on or near a shape outline; filled interior points are not always considered hits.
- The GUI is a visualization layer and intentionally does not replace the CLI.
- Generated log files and compiled classes are ignored by Git.

## Future Improvements

- Add exact geometric intersection algorithms for shape pairs
- Add export/import for command scripts
- Add generated screenshots after a verified GUI demo
- Split legacy console printing into a cleaner view/output abstraction

## Acknowledgement

This project started as an object-oriented programming course project and was later cleaned up for public portfolio presentation. Coursework-only artifacts and private identity information are intentionally excluded from this repository.

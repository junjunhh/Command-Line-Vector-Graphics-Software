# Clevis User Manual

Clevis commands are entered one per line. Command keywords, including `quit`, are case-insensitive; shape names are case-sensitive.

Names may contain letters, numbers, and underscores. Numeric arguments use ordinary decimal numbers such as `10`, `-5`, or `12.5`.

## Shape Creation

### Rectangle

```text
rectangle <name> <x> <y> <width> <height>
```

Creates a rectangle using top-left coordinate `(x, y)`.

Example:

```text
rectangle r1 10 10 120 80
```

Width and height must be positive.

### Square

```text
square <name> <x> <y> <length>
```

Creates a square using top-left coordinate `(x, y)`.

Example:

```text
square s1 40 40 60
```

Length must be positive.

### Circle

```text
circle <name> <x> <y> <radius>
```

Creates a circle using center coordinate `(x, y)`.

Example:

```text
circle c1 220 120 50
```

Radius must be positive.

### Line

```text
line <name> <x1> <y1> <x2> <y2>
```

Creates a line segment from `(x1, y1)` to `(x2, y2)`.

Example:

```text
line l1 0 0 300 200
```

## Shape Management

### Group

```text
group <group_name> <shape1> <shape2> ...
```

Groups existing top-level shapes. Grouped member shapes cannot be manipulated directly until ungrouped.

Example:

```text
group g1 r1 c1
```

### Ungroup

```text
ungroup <group_name>
```

Releases shapes from a group.

Example:

```text
ungroup g1
```

### Delete

```text
delete <name>
```

Deletes a top-level shape or group.

Example:

```text
delete r1
```

### Move

```text
move <name> <dx> <dy>
```

Moves a top-level shape or group by an offset.

Example:

```text
move g1 20 10
```

## Queries

### Bounding Box

```text
boundingbox <name>
```

Prints the axis-aligned bounding box of a top-level shape or group.

Example:

```text
boundingbox g1
```

### Shape At

```text
shapeat <x> <y>
```

Finds the topmost shape whose outline is at or near the point. Filled interiors are not always treated as hits.

Example:

```text
shapeat 10 10
```

### Intersect

```text
intersect <name1> <name2>
```

Checks whether two top-level shapes intersect. Current implementation uses bounding box overlap.

Example:

```text
intersect r1 c1
```

### List

```text
list <name>
```

Prints details for one top-level shape or group.

Example:

```text
list r1
```

### List All

```text
listall
```

Prints all top-level shapes in reverse z-order.

Example:

```text
listall
```

## History

### Undo

```text
undo
```

Reverts the previous modifying command where possible.

### Redo

```text
redo
```

Reapplies the most recently undone command where possible.

## General Commands

### Help

```text
help
```

Prints a command summary in the CLI.

### Quit

```text
quit
```

Exits the CLI. In the GUI, `quit` closes the window. The keyword is case-insensitive, so `QUIT`, `Quit`, and `qUiT` are also accepted.

## Common Errors

- Unknown command: the command keyword is not supported.
- Incorrect argument count: the command does not match the required format.
- Invalid name: a name contains unsupported characters.
- Duplicate name: a shape or group already uses that name.
- Missing shape: the requested shape or group does not exist.
- Locked member: a shape inside a group cannot be directly moved, deleted, listed, or queried.
- Invalid dimension: width, height, radius, or square length must be positive.

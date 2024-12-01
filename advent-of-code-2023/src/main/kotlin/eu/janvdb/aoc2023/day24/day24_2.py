import re

from z3 import IntVector, Solver

# Read the input
with open("../../../../../../../inputs/input24.txt") as file:
    lines = file.read().strip().split("\n")
hailstones = [list(map(int, re.findall("-?\d+", line))) for line in lines]

hailstones = hailstones[0:4] # also works with only a subset of the hailstones and is much faster

# Use z3 to solve part 2
x, y, z, vx, vy, vz = IntVector("rock", 6)
times = IntVector("times", len(hailstones))
solver = Solver()

for time, (hx, hy, hz, hvx, hvy, hvz) in zip(times, hailstones):
    solver.add(x + vx * time == hx + hvx * time)
    solver.add(y + vy * time == hy + hvy * time)
    solver.add(z + vz * time == hz + hvz * time)

solver.check()
model = solver.model()

print(model[x])
print(model[y])
print(model[z])
print(model[x].as_long() + model[y].as_long() + model[z].as_long())

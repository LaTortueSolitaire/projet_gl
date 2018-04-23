#!/usr/bin/env python3
import matplotlib.pyplot as plt
from matplotlib import style
import math
style.use('ggplot')

print("Reading input")
# with open("outFromCos.txt","r") as f:
f = open("outFromCos.txt", "r")
f2 = open("outAbscisses.txt", "r")
res = f.read()
res = eval(res)
abscisses = f2.read()
abscisses = eval(abscisses)


error_abs = [abs(res[i] - math.cos(abscisses[i])) for i in range(len(abscisses))]
# error_abs = [abs(res[i] - round(math.cos(abscisses[i]), 7)) for i in range(len(abscisses))]

# plt.scatter(abscisses, res, s=10, color="G")
# plt.show()

plt.scatter(abscisses, error_abs, s=1, color="G")
plt.title('Absolute Error between Python cos and our cos')
plt.ylim([0, 0.0000006])
plt.show()
f.close()
f2.close()

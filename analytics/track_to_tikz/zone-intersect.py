#!/usr/bin/env python3
import xml.etree.ElementTree as ET
import math
import mpu
import utm

p1 = (50.643185, 11.999653)
p2 = (50.642576, 12.000445)

grid_points_a = []
grid_points_b = []

sep_line = ((50.643185, 12.0), (50.642576, 12.0))

for x in range(60):
    for y in range(60):
        utm_x = 712005 + x * 5
        utm_y = 5614438 - y * 5
        utm_zone = 32
        coord = utm.to_latlon(utm_x, utm_y, 32, northern=True)
        coord_nb_top = utm.to_latlon(utm_x + 5, utm_y + 5, utm_zone, northern=True)
        coord_nb_left = utm.to_latlon(utm_x + 5, utm_y, utm_zone, northern=True)

        if coord[1] < sep_line[0][1]:
            grid_points_a.append((coord, coord_nb_top, coord_nb_left))


for x in range(60):
    for y in range(60):
        utm_x = 288022 - x * 5
        utm_y = 5614258 + y * 5
        utm_zone = 33
        coord = utm.to_latlon(utm_x, utm_y, utm_zone, northern=True)
        coord_nb_top = utm.to_latlon(utm_x, utm_y + 5, utm_zone, northern=True)
        coord_nb_left = utm.to_latlon(utm_x + 5, utm_y, utm_zone, northern=True)

        if coord_nb_left[1] > sep_line[0][1]:
            grid_points_b.append((coord, coord_nb_top, coord_nb_left))

bounds = (
    min(p1[0], p2[0]),
    max(p1[0], p2[0]),
    min(p1[1], p2[1]),
    max(p1[1], p2[1])
)

bound_length = mpu.haversine_distance((p1[0], p1[1]), (p1[0], p2[1])) * 1000

print(bound_length)

scale_factor = 5

def node_check_bounds(lat, lon):
    return bounds[0] < lat < bounds[1] and bounds[2] < lon < bounds[3]

def lat_to_y(lat):
    return ((lat - bounds[0]) / (bounds[1] - bounds[0])) * scale_factor

def lon_to_x(lon):
    return ((lon - bounds[2]) / (bounds[3] - bounds[2])) * scale_factor

tikz_file = open('/home/mru/Documents/Studium/BA/Arbeit/intersect.tex', 'w')
tikz_file.write('\\begin{tikzpicture}\n')

# Draw gridpoints
for gp in grid_points_a:
    if node_check_bounds(gp[0][0], gp[0][1]) and node_check_bounds(gp[1][0], gp[1][1]):
        x = lon_to_x(gp[0][1])
        y = lat_to_y(gp[0][0])
        tx = lon_to_x(gp[1][1])
        ty = lat_to_y(gp[1][0])
        lx = lon_to_x(gp[2][1])
        ly = lat_to_y(gp[2][0])

        tikz_file.write('\\draw [blue] (' + str(lx)[:6] + ',' + str(ly)[:6] + ') -- (' + str(tx)[:6] + ',' + str(ty)[:6] + ');\n')
        tikz_file.write('\\draw [blue] (' + str(x)[:6] + ',' + str(y)[:6] + ') -- (' + str(lx)[:6] + ',' + str(ly)[:6] + ');\n')


# Draw gridpoints
for gp in grid_points_b:
    if node_check_bounds(gp[0][0], gp[0][1]) and node_check_bounds(gp[1][0], gp[1][1]):
        x = lon_to_x(gp[0][1])
        y = lat_to_y(gp[0][0])
        tx = lon_to_x(gp[1][1])
        ty = lat_to_y(gp[1][0])
        lx = lon_to_x(gp[2][1])
        ly = lat_to_y(gp[2][0])

        tikz_file.write('\\draw [red] (' + str(x)[:6] + ',' + str(y)[:6] + ') -- (' + str(tx)[:6] + ',' + str(ty)[:6] + ');\n')
        tikz_file.write('\\draw [red] (' + str(x)[:6] + ',' + str(y)[:6] + ') -- (' + str(lx)[:6] + ',' + str(ly)[:6] + ');\n')

# Zone separation line
s1x = lon_to_x(sep_line[0][1])
s1y = lat_to_y(sep_line[0][0])
s2x = lon_to_x(sep_line[1][1])
s2y = lat_to_y(sep_line[1][0])

tikz_file.write('\\draw [line width=0.5mm, black] (' + str(s1x)[:6] + ',' + str(s1y)[:6] + ') -- (' + str(s2x)[:6] + ',' + str(s2y)[:6] + ');\n')

tikz_file.write('\\end{tikzpicture}')
tikz_file.close()

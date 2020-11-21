#!/usr/bin/env python3
import xml.etree.ElementTree as ET
import math
import mpu
import utm

vector_waypoints = []

p1 = (48.166409, 11.576249)
p2 = (48.165192, 11.578272)

grid_points = []

for x in range(60):
    for y in range(60):
        coord = utm.to_latlon(691535 + x * 5, 5337730 + y * 5, 32, northern=True)
        grid_points.append(coord)

highlighted_grid_point = (utm.to_latlon(691550 + 130, 5337750 + 150, 32, northern=True))

bounds = (
    min(p1[0], p2[0]),
    max(p1[0], p2[0]),
    min(p1[1], p2[1]),
    max(p1[1], p2[1])
)

bound_length = mpu.haversine_distance((p1[0], p1[1]), (p1[0], p2[1])) * 1000

print(bound_length)

scale_factor = 10

def node_check_bounds(lat, lon):
    return bounds[0] < lat < bounds[1] and bounds[2] < lon < bounds[3]

def lat_to_y(lat):
    return ((lat - bounds[0]) / (bounds[1] - bounds[0])) * scale_factor

def lon_to_x(lon):
    return ((lon - bounds[2]) / (bounds[3] - bounds[2])) * scale_factor

osm_file = ET.parse('munich.osm')
root_node = osm_file.getroot()

visible_nodes = {}
ways = []

# Find ways and nodes
for child in root_node:
    tag = child.tag
    if tag == 'node':
        if 'lat' in child.attrib and 'lon' in child.attrib:
            lat = float(child.attrib['lat'])
            lon = float(child.attrib['lon'])
            id = child.attrib['id']
            if node_check_bounds(lat, lon):
                visible_nodes[id] = (lat, lon)
    if tag == 'way':
        way_nodes = []
        way_type = 0
        for way_child in child:
            if way_child.tag == 'nd':
                ref = way_child.attrib['ref']
                if ref in visible_nodes:
                    node = visible_nodes[ref]
                    way_nodes.append(node)
            if way_child.tag == 'tag':
                if way_child.attrib['k'] == 'name':
                    way_type = 1
        if len(way_nodes) > 0:
            ways.append((way_type, way_nodes))

tikz_file = open('/home/matthias/Documents/Studium/BA/Arbeit/grid.tex', 'w')
tikz_file.write('\\begin{tikzpicture}\n')

# Format for tikz
# Draw ways and structures
for way in ways:
    stmt = ''
    if way[0] == 0:
        stmt = '\draw[line width=0.2mm,  black] '
        #stmt = '\draw[line width=0.02mm,  gray] '
    if way[0] == 1:
        pass
        #stmt = '\draw[line width=0.2mm,  black] '

    for i, point in enumerate(way[1]):
        x = lon_to_x(point[1])
        y = lat_to_y(point[0])

        if i > 0:
            stmt += ' -- '

        stmt += '(' + str(x)[:6] + ',' + str(y)[:6] + ')'

    stmt += ';\n'
    tikz_file.write(stmt)

# Draw gridpoints
for gp in grid_points:
    if node_check_bounds(gp[0], gp[1]):
        x = lon_to_x(gp[1])
        y = lat_to_y(gp[0])

        tikz_file.write('\\draw [blue, fill=blue] (' + str(x)[:6] + ',' + str(y)[:6] + ') circle (0.3mm);\n')

# Highlighted gridpoint
x = lon_to_x(highlighted_grid_point[1])
y = lat_to_y(highlighted_grid_point[0])
tikz_file.write('\\draw [red, fill=red] (' + str(x)[:6] + ',' + str(y)[:6] + ') circle (0.4mm);\n')
tikz_file.write('\\draw [red] (' + str(x)[:6] + ',' + str(y)[:6] + ') circle (1mm);\n')

# Draw vector waypoints
for wp in vector_waypoints:
    if node_check_bounds(wp[0], wp[1]):
        x = lon_to_x(wp[1])
        y = lat_to_y(wp[0])

        bearing = wp[3]
        dir_x = math.sin((bearing / 360) * 2 * math.pi) * 0.3 + x
        dir_y = math.cos((bearing / 360) * 2 * math.pi) * 0.3 + y

        tikz_file.write('\\draw [red, fill=red] (' + str(x)[:6] + ',' + str(y)[:6] + ') circle (0.5mm);\n')
        tikz_file.write('\\draw [red!50] (' + str(x)[:6] + ',' + str(y)[:6] + ') circle (' + str((wp[2] / bound_length) * scale_factor) + ');\n')
        tikz_file.write('\\draw [-> width=0.7mm, red] (' + str(x)[:6] + ',' + str(y)[:6] + ') -- (' + str(dir_x)[:6] + ',' + str(dir_y)[:6] + ');\n')

tikz_file.write('\\end{tikzpicture}')
tikz_file.close()

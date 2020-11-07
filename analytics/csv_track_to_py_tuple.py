#!/usr/bin/env python3
import csv

with open('/home/matthias/Downloads/20201106_154145.csv') as csvf:
    csv_reader = csv.reader(csvf, delimiter=';')

    delays = []
    num_items = 0

    prev_date = None

    for row in reversed(list(csv_reader)):
        if row[3] == 'Location Update':
            print('(', end = '')
            print(str(row[4]) + ',', end = '')
            print(str(row[5]) + ',', end = '')
            print(str(row[6]) + ',', end = '')
            print(str(row[7]) + ',', end = '')
            print(str(row[8]) + '', end = '')
            print('),')
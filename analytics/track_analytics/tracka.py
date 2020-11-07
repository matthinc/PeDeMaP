#!/usr/bin/env python3

import csv
from datetime import datetime
import statistics

with open('/home/matthias/Desktop/20201105_145559.csv') as csvf:
    csv_reader = csv.reader(csvf, delimiter=';')

    delays = []
    num_items = 0

    prev_date = None

    for row in reversed(list(csv_reader)):

        if row[3] == 'Location' and len(row) == 7:
            date = datetime.fromtimestamp(int(row[1]))

            num_items += 1

            if not (prev_date is None):
                diff = (date - prev_date).total_seconds()
                delays.append(diff)

            prev_date = date


    print("Number of items:    " + str(num_items))
    print("Duration:           " + str(sum(delays)))
    print("Duration (mean):    " + str(statistics.mean(delays)))
    print("Duration (meadian): " + str(statistics.median(delays)))
    print("Duration (max):     " + str(max(delays)))
    print("Duration (variance):" + str(statistics.variance(delays)))
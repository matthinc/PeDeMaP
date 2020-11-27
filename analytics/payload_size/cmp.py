#!/usr/bin/env python3

import json
import payload_pb2
import gzip
import random
import math
import statistics
import matplotlib.pyplot as plt
import numpy as np

def provide_random_data():
    MAXINT = 2147483648
    example_payload = dict()
    example_payload['device_id'] = random.randint(0, MAXINT)
    example_payload['timestamp'] = random.randint(0, MAXINT)
    example_payload['zone_id'] = random.randint(0, 60)
    example_payload['hemisphere'] = (random.randint(0, 1) == 0)
    example_payload['northing'] = random.randint(0, 20000000)
    example_payload['easting'] = random.randint(0, 700000)
    example_payload['bearing'] = random.randint(0, 360)
    example_payload['speed'] = random.randint(0, 150)
    example_payload['accuracy'] = random.random() * 100.0
    return example_payload

def to_json(payload):
    return json.dumps(payload)

def to_proto(paylaod):
    payload = payload_pb2.Payload()
    payload.device_id = paylaod['device_id']
    payload.timestamp = paylaod['timestamp']
    payload.zone_id = paylaod['zone_id']
    payload.hemisphere = paylaod['hemisphere']
    payload.northing = paylaod['northing']
    payload.easting = paylaod['easting']
    payload.bearing = paylaod['bearing']
    payload.speed = paylaod['speed']
    payload.accuracy = paylaod['accuracy']
    return payload.SerializeToString()


sizes = []

for _ in range(0, 10000):
    payload = provide_random_data()
    
    payload_json = to_json(payload)
    payload_proto = to_proto(payload)
    payload_json_zip = gzip.compress(payload_json.encode('utf-8'))
    payload_proto_zip = gzip.compress(payload_proto)

    sizes.append(
        (len(payload_json.encode('utf-8')), len(payload_json_zip), len(payload_proto), len(payload_proto_zip))
    )


means = []
mins = []
maxs = []

for component in range(0, 4):
    values = list(map(lambda v : v[component], sizes))
    print('Median: ' + str(statistics.median(values)))
    print('Min: ' + str(min(values)))
    print('Max: ' + str(max(values)))

    means.append(statistics.median(values))
    mins.append(min(values))
    maxs.append(max(values))

_, ax = plt.subplots()
ax.errorbar(["JSON", "JSON (gz)", "Proto", "Proto (gz)"], np.array(means), [np.array(means) - np.array(mins), np.array(maxs) - np.array(means)], fmt='ok', lw=1, capsize=2, ecolor="gray")
plt.xlim(-1, 4)
plt.ylabel("Size in Bytes")
plt.savefig('histogram.png', dpi=400)

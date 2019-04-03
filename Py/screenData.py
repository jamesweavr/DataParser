import csv
import json

open("outputData.csv", "w").close()

def main():
	dates = {}
	errorData = []
	i = 0
	with open('Resources/ScreenData.json') as json_file:
		#variable to ensure proper data order
		onOff = 'off'

		data = json.load(json_file)
		startTime = 0

		for p in data['list']:
			i += 1
			#Check for errors in data order
			if(p['power'] == onOff):
				print('On off at date: ' + p['date'] + ' time: ' + p['time'])
				errorData.append(i)
				continue

			#Set start time
			if (p['power'] == "on"):
				startTime = int(p['sec'])
				onOff = 'on'

			#Calculate total time with start and end time
			elif (p['power'] == "off"):

				dates[p['date']] = dates.setdefault(p['date'], 0) + (int(p['sec']) - startTime)/60
				onOff = 'off'

			else:
				print('Power is not on or off')


			#print('Power: ' + p['power'])
			#print('Date: ' + p['date'])
			#print('Time: ' + p['time'])
			#print('')

	with open('outputData.csv', 'a') as f:
		for key, value in dates.items():
			f.write(key + "," + str(value) + "\n")

	i = -1
	with open("Resources/ScreenData.json", "r") as f:
		lines = f.readlines()
	with open("Resources/ScreenData.json", "w") as f:
		for line in lines:
			i += 1
			if not i in errorData:
				f.write(line)
			else:
				print(line)




main()

#!/bin/bash
fin=$1
colors=(black blue brown cyan gray green lightblue lime magenta orange pink purple red silver white yellow)
for color in "${colors[@]}"
do
	fou=${fin/COLOR/$color}
	cp "$fin" "$fou"
	sed -i "s/COLOR/$color/g" "$fou"
done
#!/bin/bash

docker buildx build --no-cache --push --platform linux/amd64 --tag jonitooo/flexo:1.0.11 .
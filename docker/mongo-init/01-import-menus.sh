#!/usr/bin/env bash
# 把 menus.json 导入 local 数据库的 menus 集合
echo "=== Importing menus.json ==="
mongoimport \
  --db local \
  --collection menus \
  --file /docker-entrypoint-initdb.d/menus.json \
  --jsonArray
echo "=== menus.json imported ==="

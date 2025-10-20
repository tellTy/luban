#!/bin/bash

# API测试脚本

# 基础URL
BASE_URL="http://localhost:7573"

# 测试用户凭证
USER_CREDENTIALS='{"username": "user_1", "password": "user_1"}'
EDITOR_CREDENTIALS='{"username": "editor_1", "password": "editor_1"}'
ADMIN_CREDENTIALS='{"username": "adm_1", "password": "adm_1"}'

# 获取用户令牌
echo "=== 获取用户令牌 ==="
USER_TOKEN=$(curl -s -X POST "$BASE_URL/auth/login" \
  -H "Content-Type: application/json" \
  -d "$USER_CREDENTIALS" | jq -r '.token')
echo "用户令牌: $USER_TOKEN"
echo

# 获取编辑者令牌
echo "=== 获取编辑者令牌 ==="
EDITOR_TOKEN=$(curl -s -X POST "$BASE_URL/auth/login" \
  -H "Content-Type: application/json" \
  -d "$EDITOR_CREDENTIALS" | jq -r '.token')
echo "编辑者令牌: $EDITOR_TOKEN"
echo

# 获取管理员令牌
echo "=== 获取 获取管理员令牌 ==="
ADMIN_TOKEN=$(curl -s -X POST "$BASE_URL/auth/login" \
  -H "Content-Type: application/json" \
  -d "$ADMIN_CREDENTIALS" | jq -r '.token')
echo "管理员令牌: $ADMIN_TOKEN"
echo

# 获取 all products
echo "=== 获取所有产品 ==="
curl -s -X GET "$BASE_URL/products" \
  -H "Authorization: Bearer $USER_TOKEN" | jq
echo

# create product
echo "=== 创建 测试创建产品 ==="
# user cannot create product
echo "用户尝试创建产品 (应该失败):"
curl -s -X POST "$BASE_URL/products" \
  -H "Authorization: Bearer $USER_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name": "测试产品"}' | jq
echo

# editor can create product
echo "编辑者创建产品 (应该成功):"
CREATE_RESPONSE=$(curl -s -X POST "$BASE_URL/products" \
  -H "Authorization: Bearer $EDITOR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name": "测试产品"}')
echo "$CREATE_RESPONSE" | jq
PRODUCT_ID=$(echo "$CREATE_RESPONSE" | jq -r '.id')
echo "创建的产品ID: $PRODUCT_ID"
echo

# update product
echo "=== 测试更新产品 ==="
# user cannot update product
echo "用户尝试更新产品 (应该失败):"
curl -s -X PUT "$BASE_URL/products/$PRODUCT_ID" \
  -H "Authorization: Bearer $USER_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name": "更新的产品名"}' | jq
echo

# editor can update product
echo "编辑者更新产品 (应该成功):"
curl -s -X PUT "$BASE_URL/products/$PRODUCT_ID" \
  -H "Authorization: Bearer $EDITOR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name": "更新的产品名"}' | jq
echo

# delete product
echo "=== 测试删除产品 ==="
# user cannot delete product
echo "用户尝试删除产品 (应该失败):"
curl -s -X DELETE "$BASE_URL/products/$PRODUCT_ID" \
  -H "Authorization: Bearer $USER_TOKEN" | jq
echo

# editor can delete product
echo "编辑者删除产品 (应该成功):"
curl -s -X DELETE "$BASE_URL/products/$PRODUCT_ID" \
  -H "Authorization: Bearer $EDITOR_TOKEN" | jq
echo



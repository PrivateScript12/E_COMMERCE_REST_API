#!/bin/bash

echo "=== E-commerce REST API Registration Testing ==="
echo

BASE_URL="http://localhost:8080/api/auth"

echo "1. Testing successful registration..."
curl -X POST $BASE_URL/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser1","email":"test1@example.com","password":"password123"}' \
  -s | jq .
echo

echo "2. Testing duplicate username..."
curl -X POST $BASE_URL/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser1","email":"test2@example.com","password":"password123"}' \
  -s | jq .
echo

echo "3. Testing duplicate email..."
curl -X POST $BASE_URL/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser2","email":"test1@example.com","password":"password123"}' \
  -s | jq .
echo

echo "4. Testing empty username..."
curl -X POST $BASE_URL/register \
  -H "Content-Type: application/json" \
  -d '{"username":"","email":"empty@example.com","password":"password123"}' \
  -s | jq .
echo

echo "5. Testing short username..."
curl -X POST $BASE_URL/register \
  -H "Content-Type: application/json" \
  -d '{"username":"ab","email":"short@example.com","password":"password123"}' \
  -s | jq .
echo

echo "6. Testing invalid email..."
curl -X POST $BASE_URL/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser3","email":"invalid-email","password":"password123"}' \
  -s | jq .
echo

echo "7. Testing short password..."
curl -X POST $BASE_URL/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser4","email":"test4@example.com","password":"123"}' \
  -s | jq .
echo

echo "8. Testing multiple validation errors..."
curl -X POST $BASE_URL/register \
  -H "Content-Type: application/json" \
  -d '{"username":"ab","email":"invalid","password":"123"}' \
  -s | jq .
echo

echo "9. Testing successful login with registered user..."
curl -X POST $BASE_URL/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser1","password":"password123"}' \
  -s | jq .
echo

echo "10. Testing login with wrong password..."
curl -X POST $BASE_URL/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser1","password":"wrongpassword"}' \
  -s | jq .
echo

echo "=== Testing Complete ==="

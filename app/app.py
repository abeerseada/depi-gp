from flask import Flask, render_template, request, redirect, url_for, jsonify
from pymongo import MongoClient
from bson.objectid import ObjectId
from dotenv import load_dotenv
import os

load_dotenv() 

app = Flask(__name__)

MONGO_URI = os.getenv("MONGO_URI", "mongodb://localhost:27017")
DB_NAME = os.getenv("DB_NAME", "mydb")

client = MongoClient(MONGO_URI)
db = client[DB_NAME]
items_col = db["items"]  

@app.route("/")
def index():
    items = list(items_col.find())
    return render_template("index.html", items=items)

@app.route("/add", methods=["POST"])
def add_item():
    name = request.form.get("name")
    if not name:
        return "name required", 400
    result = items_col.insert_one({"name": name})
    return redirect(url_for("index"))


@app.route("/item/<id>", methods=["GET"])
def get_item(id):
    try:
        item = items_col.find_one({"_id": ObjectId(id)})
    except Exception:
        return "invalid id", 400
    if not item:
        return "not found", 404
    item["_id"] = str(item["_id"])
    return jsonify(item)


@app.route("/item/<id>/update", methods=["POST"])
def update_item(id):
    name = request.form.get("name")
    if not name:
        return "name required", 400
    try:
        res = items_col.update_one({"_id": ObjectId(id)}, {"$set": {"name": name}})
    except Exception:
        return "invalid id", 400
    if res.matched_count == 0:
        return "not found", 404
    return redirect(url_for("index"))


@app.route("/item/<id>/delete", methods=["POST"])
def delete_item(id):
    try:
        res = items_col.delete_one({"_id": ObjectId(id)})
    except Exception:
        return "invalid id", 400
    if res.deleted_count == 0:
        return "not found", 404
    return redirect(url_for("index"))

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5051, debug=True)

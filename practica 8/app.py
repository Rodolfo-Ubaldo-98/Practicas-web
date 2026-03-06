from flask import Flask, jsonify
from flask_cors import CORS
from pymongo import MongoClient

app = Flask(__name__)
CORS(app)

# Conexion MongoDB
cliente = MongoClient("mongodb://localhost:27017/")
db = cliente["cartelera_cultural"]
coleccion = db["eventos"]

@app.route("/")
def inicio():
    return "API Cartelera Cultural funcionando"

@app.route("/eventos", methods=["GET"])
def obtener_eventos():
    eventos = list(coleccion.find({}, {"_id": 0}))
    return jsonify(eventos)

if __name__ == "__main__":
    app.run(debug=True)
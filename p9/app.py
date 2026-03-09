from flask import Flask, request, jsonify
from flask_cors import CORS
from pymongo import MongoClient

app = Flask(__name__)
CORS(app)

# Conexión a MongoDB
client = MongoClient("mongodb://localhost:27017/")
db = client["EscuelaDB"]
coleccion = db["Alumnos"]

# Ruta por default
@app.route('/')
def inicio():
    return "API UPP funcionando"

# Ruta para consultar alumnos
@app.route('/alumnos', methods=['GET'])
def obtener_alumnos():
    alumnos = list(coleccion.find({}, {"_id": 0}))
    return jsonify(alumnos)

if __name__ == "__main__":
    app.run(debug=True)
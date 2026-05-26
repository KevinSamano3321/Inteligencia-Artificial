import tensorflow as tf
import numpy as np
import matplotlib.pyplot as plt
from tensorflow.keras.preprocessing import image

class_names = ['ben_afflek', 'elton_john', 'jerry_seinfel', 'madonna', 'mindy_kaling']

def predict_celebrity(img_path, model_path='celebrity_model.h5'):

    model = tf.keras.models.load_model(model_path)
    img = image.load_img(img_path, target_size=(160, 160))

    img_array = image.img_to_array(img) / 255.0
    img_array = np.expand_dims(img_array, axis=0)

    predictions = model.predict(img_array)
    score = np.max(predictions)
    class_idx = np.argmax(predictions)
    
    plt.imshow(img)
    plt.title(f"Prediction: {class_names[class_idx]} ({100 * score:.2f}%)")
    plt.axis('off')
    plt.show()

ruta_imagen = r'data\train\madonna\httpimagegaladevcmseamadonnaprivatdetektivsquaretopsquarejpgv.jpg'
predict_celebrity(ruta_imagen)
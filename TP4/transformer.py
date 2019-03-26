import numpy as np
from skimage.measure import compare_ssim as ssim
from skimage import transform
from skimage import exposure
from skimage import filters
from skimage import restoration
from skimage import util
import random
import math

def rotate(img, rad_angle):
    # to complete
    #pass
    image = np.copy(img)
    return transform.AffineTransform(image, rotation=rad_angle) #transform.rotate(img, math.degrees(rad_angle))

def translate(img, trans_x, trans_y):
    # to complete
    #pass
    image = np.copy(img)
    return transform.AffineTransform(image, translation=(trans_x, trans_y))

def scale(img, scale_1, scale_2):
    # to complete 
    #pass
    image = np.copy(img)
    return transform.AffineTransform(image, scale=(scale_1, scale_2))

def shear(img, factor):
    # to complete 
    #pass
    image = np.copy(img)
    return transform.AffineTransform(image, shear=factor)
    
def blur(img, sigma):
    # to complete
    #pass
    image = np.copy(img)
    return filters.gaussian(image, sigma=sigma)

def change_brightness(img, factor):
    # to complete
    #pass
    image = np.copy(img)
    return exposure.adjust_gamma(image, gain=factor)

def change_contrast(img, factor):
    # to complete 
    #pass
    image = np.copy(img)
    return exposure.adjust_gamma(image, gamma=factor)

def change_sharpness(img, factor):
    # to complete
    #pass
    image = np.copy(img)
    return restoration.denoise_bilateral(image, sigma_spatial=factor)

def add_random_noise(img, delta, proba):
    # to complete
    #pass
    image = np.copy(img)
    return util.random_noise(image, mean=delta, amount=proba)

def build_random_transformation():
    # to complete
    #pass
    numberOfTransformations = random.randint(1,10) #random size between 1 and 9
    transformation = [] #This list will contain random unique numbers between 1 and 9. Size of the array will also be random.
    for i in range(0, numberOfTransformations):
        rand = random.randint(1,10)
        while rand in transformation:
            rand = random.random(1,10)
        transformation.append(rand)
    return transformation

def compute_similarity(first_img, second_img):
    # to complete
    #pass
    return ssim(first_img, second_img)

#Question : similarity value sur quel transformatioN? Je peux faire un array qui va contenir tous les sim values a chaque etape de la transformation?
def apply_transformation(image_origin, transformation):
    image = np.copy(image_origin)
    transformed_data = []
    similarity_values = []
    last = image
    for i in transformation:
        if len(transformed_data) > 0:
            last = transformed_data[-1]
        if i == 1: #rotate
            rotatedImg = rotate(last, 2 * math.pi * random.random())
            transformed_data.append(rotatedImg)
            similarity_values.append(compute_similarity(image_origin, rotatedImg))
        elif i == 2: #translate, maximum translation is half of x and half of y
            y, x = last.shape
            translatedImg = translate(last, random.randint(x/2), random.randint(y/2))
            transformed_data.append(translatedImg)
            similarity_values.append(compute_similarity(image_origin, translatedImg))
        elif i == 3: #scale. Scale factors are between 0.5 and 2, chosen arbitrarily.
            scaledImg = scale(last, 2 * random.rand() + 0.5, 2 * random.rand() + 0.5)
            transformed_data.append(scaledImg)
            similarity_values.append(compute_similarity(image_origin, scaledImg))
        elif i == 4: #shear
            shearedImg = shear(last,  2 * math.pi * random.random())
            transformed_data.append(shearedImg)
            similarity_values.append(compute_similarity(image_origin, shearedImg))
        elif i == 5: #blur
            bluredImg = blur(last, random.randint(10))
            transformed_data.append(bluredImg)
            similarity_values.append(compute_similarity(image_origin, bluredImg))
        elif i == 6: #change_brightness
            changedBrightnessImg = change_brightness(last, 2 * random.random())
            transformed_data.append(changedBrightnessImg)
            similarity_values.append(compute_similarity(image_origin, changedBrightnessImg))
        elif i == 7: #change_contrast
            changedContrastImg = change_contrast(last, 2 * random.random())
            transformed_data.append(changedContrastImg)
            similarity_values.append(compute_similarity(image_origin, changedContrastImg))
        elif i == 8: #change_sharpness
            sharpenedImage = change_sharpness(last, 5 * random.random())
            transformed_data.append(sharpenedImage)
            similarity_values.append(compute_similarity(image_origin, sharpenedImage))
        else: #add_random_noise
            noisedImage = add_random_noise(last, 2 * random.random(), 0.1 * random.random())
            transformed_data.append(noisedImage)
            similarity_values.append(compute_similarity(image_origin, noisedImage))
    return transformed_data, similarity_values[0]


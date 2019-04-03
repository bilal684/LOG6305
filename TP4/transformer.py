import numpy as np
from skimage.measure import compare_ssim as ssim
from skimage import transform
from skimage import exposure
from skimage import filters
from skimage.morphology import disk
from skimage import util
from skimage.filters.rank import mean_bilateral
import random
import math

def rotate(img, rad_angle): #affine
    # to complete
    image = np.copy(img)
    y, x = image.shape
    shift_y = (y - 1) / 2.
    shift_x = (x - 1) / 2.
    tf_rotate = transform.SimilarityTransform(rotation=rad_angle)
    tf_shift = transform.SimilarityTransform(translation=[-shift_x, -shift_y])
    tf_shift_inv = transform.SimilarityTransform(translation=[shift_x, shift_y])
    image_rotated = transform.warp(image, (tf_shift + (tf_rotate + tf_shift_inv)).inverse, order=3)
    return image_rotated

def translate(img, trans_x, trans_y): #affine
    # to complete
    #pass
    image = np.copy(img)
    tf = transform.AffineTransform(translation=(trans_x, trans_y))
    return transform.warp(image, tf)

def scale(img, scale_1, scale_2): #affine
    # to complete 
    #pass
    image = np.copy(img)
    tf = transform.AffineTransform(scale=(scale_1, scale_2))
    return transform.warp(image, tf)

def shear(img, factor): #affine
    # to complete 
    #pass
    image = np.copy(img)
    tf = transform.AffineTransform(shear=factor)
    return transform.warp(image, tf)
    
def blur(img, sigma): #pixel
    # to complete
    #pass
    image = np.copy(img)
    return filters.gaussian(image, sigma=sigma)

def change_brightness(img, factor): #pixel
    # to complete
    #pass
    image = np.copy(img)
    return exposure.adjust_gamma(image, gain=factor)

def change_contrast(img, factor): #pixel
    # to complete 
    #pass
    image = np.copy(img)
    return exposure.adjust_gamma(image, gamma=factor)

#See : http://scikit-image.org/docs/0.12.x/auto_examples/xx_applications/plot_rank_filters.html
def change_sharpness(img, factor): #pixel
    image = np.copy(img)
    return mean_bilateral(image, disk(factor))

def add_random_noise(img, delta, proba): #pixel
    # to complete
    #pass
    image = np.copy(img)
    return util.random_noise(image, mean=proba, var=delta)

def build_random_transformation(): #pixel
    # to complete
    #pass
    numberOfTransformations = random.randint(1,5) #random size between 1 and 9
    transformation = [] #This list will contain random unique numbers between 1 and 9. Size of the array will also be random.
    for i in range(0, numberOfTransformations):
        rand = random.randint(1,5)
        while rand in transformation:
            rand = random.randint(1,5)
        transformation.append(rand)
    return transformation

def compute_similarity(first_img, second_img): #TODO ask him what the use of this
    # to complete
    #pass
    return ssim(first_img, second_img)

#Question : similarity value sur quel transformatioN? Je peux faire un array qui va contenir tous les sim values a chaque etape de la transformation?
def apply_transformation(image_origin, transformation):
    transformed_data = []
    image = np.copy(image_origin)
    for i in transformation:
        if i == 1: #blur - pixel
            image = blur(image, random.randint(1,10))
        elif i == 2: #change_brightness - pixel
            image = change_brightness(image, random.random())
        elif i == 3: #change_contrast - pixel
            image = change_contrast(image, 1.5 * random.random() + 0.5)
        elif i == 4: #change_sharpness - pixel
            image = change_sharpness(image, 500 * random.random())
        else: #add_random_noise - pixel
            image = add_random_noise(image, 0.25 * random.random(), 0.1 * random.random())

    transformed_data.append(image)
    sim = compute_similarity(image_origin, image)
    transformed_data.append(rotate(image, 2 * math.pi * random.random()))
    y, x = image.shape
    transformed_data.append(translate(image, random.randint(0,x/4), random.randint(0,y/4)))
    transformed_data.append(scale(image, random.random() * 1.5, random.random() * 1.5))
    transformed_data.append(shear(image,  0.5 * random.random()))
    return transformed_data, sim
import numpy as np
from skimage.measure import compare_ssim as ssim
from skimage import transform
from skimage import exposure
from skimage import filters
from skimage.morphology import disk
from skimage.filters.rank import mean_bilateral
import random

def rotate(img, rad_angle): #affine
    image = np.copy(img)
    y, x = image.shape
    shift_y = (y - 1) / 2.
    shift_x = (x - 1) / 2.
    tf_rotate = transform.AffineTransform(rotation=rad_angle)
    tf_shift = transform.AffineTransform(translation=[-shift_x, -shift_y])
    tf_shift_inv = transform.AffineTransform(translation=[shift_x, shift_y])
    image_rotated = transform.warp(image, (tf_shift + (tf_rotate + tf_shift_inv)).inverse, preserve_range=True)
    return image_rotated.astype(np.uint8)

def translate(img, trans_x, trans_y): #affine
    image = np.copy(img)
    tf = transform.AffineTransform(translation=(trans_x, trans_y))
    translatedImg = transform.warp(image, tf, preserve_range=True).astype(np.uint8)
    return translatedImg

def scale(img, scale_1, scale_2): #affine
    image = np.copy(img)
    tf = transform.AffineTransform(scale=(scale_1, scale_2))
    scaledImg = transform.warp(image, tf, preserve_range=True).astype(np.uint8)
    return scaledImg

def shear(img, factor): #affine
    image = np.copy(img)
    tf = transform.AffineTransform(shear=factor)
    shearedImg = transform.warp(image, tf, preserve_range=True).astype(np.uint8)
    return shearedImg
    
def blur(img, sigma): #pixel
    image = np.copy(img)
    blurredImg = filters.gaussian(image, sigma=sigma) * 255
    return blurredImg.astype(np.uint8)

def change_brightness(img, factor): #pixel
    image = np.copy(img)
    changedBrightnessImg = exposure.adjust_gamma(image, gain=factor).astype(np.uint8)
    return changedBrightnessImg

def change_contrast(img, factor): #pixel
    image = np.copy(img)
    changedContrastImg = exposure.adjust_gamma(image, gamma=factor).astype(np.uint8)
    return changedContrastImg

#See : http://scikit-image.org/docs/0.12.x/auto_examples/xx_applications/plot_rank_filters.html
#Un filtre bilateral permet d'ameliorer le sharpness
def change_sharpness(img, factor): #pixel
    image = np.copy(img)
    changedSharpnessImg = mean_bilateral(image, disk(factor))
    return changedSharpnessImg

def add_random_noise(img, delta, proba): #pixel
    image = np.copy(img)
    noise = np.zeros(image.shape)
    sizeY, sizeX = image.shape
    for j in range(0, sizeY):
        for i in range(0, sizeX):
            randomPixelChoiceProba = random.random()
            if randomPixelChoiceProba < proba:
                noise[j][i] += delta
    noisyImg = (image + noise).astype(np.uint8)
    return noisyImg

def build_random_transformation(): #pixel
    numberOfTransformations = random.randint(1,5) #random size between 1 and 9
    transformation = [] #This list will contain random unique numbers between 1 and 9. Size of the array will also be random.
    for i in range(0, numberOfTransformations):
        rand = random.randint(1,5)
        while rand in transformation:
            rand = random.randint(1,5)
        transformation.append(rand)
    return transformation

def compute_similarity(first_img, second_img):
    return ssim(first_img, second_img)

def apply_transformation(image_origin, transformation):
    transformed_data = []
    image = np.copy(image_origin)
    for i in transformation:
        if i == 1: #blur - pixel
            image = blur(image, random.randint(1,4))
        elif i == 2: #change_brightness - pixel
            image = change_brightness(image, random.random() + 0.5)
        elif i == 3: #change_contrast - pixel
            image = change_contrast(image, 1.5 * random.random() + 0.8)
        elif i == 4: #change_sharpness - pixel
            image = change_sharpness(image, 5 * random.random() + 0.8)
        else: #add_random_noise - pixel
            image = add_random_noise(image, 5 * random.random() + 5, random.random())

    transformed_data.append(image)
    sim = compute_similarity(image_origin, image)
    transformed_data.append(rotate(image, 0.5 * random.random() - 0.25))
    y, x = image.shape
    transformed_data.append(translate(image, random.randint(0, int(x/6)), random.randint(0, int(y/6))))
    transformed_data.append(scale(image, 0.2 * random.random() + 0.9, 0.2 * random.random() + 0.9))
    transformed_data.append(shear(image,  0.2 * random.random()))
    return transformed_data, sim
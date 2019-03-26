import numpy as np
from skimage.measure import compare_ssim as ssim
from skimage import transform
from skimage import exposure
from skimage import filters
from skimage import restoration
from skimage import util

def rotate(img, rad_angle):
    # to complete
    #pass
    return transform.AffineTransform(img, rotation=rad_angle) #transform.rotate(img, math.degrees(rad_angle))

def translate(img, trans_x, trans_y):
    # to complete
    #pass
    return transform.AffineTransform(img, translation=(trans_x, trans_y))

def scale(img, scale_1, scale_2):
    # to complete 
    #pass
    return transform.AffineTransform(img, scale=(scale_1, scale_2))

def shear(img, factor):
    # to complete 
    #pass
    return transform.AffineTransform(img, shear=factor)
    
def blur(img, sigma):
    # to complete
    #pass
    return filters.gaussian(img, sigma=sigma)

def change_brightness(img, factor):
    # to complete
    #pass
    return exposure.adjust_gamma(img, gain=factor)

def change_contrast(img, factor):
    # to complete 
    #pass
    return exposure.adjust_gamma(img, gamma=factor)

def change_sharpness(img, factor):
    # to complete
    #pass
    return restoration.denoise_bilateral(img, sigma_spatial=factor)

def add_random_noise(img, delta, proba):
    # to complete
    #pass
    return util.random_noise(img, mean=delta, amount=proba)

def build_random_transformation():
    # to complete
    #pass
    return

def compute_similarity(first_img, second_img):
    # to complete
    #pass
    return ssim(first_img, second_img)

def apply_transformation(image_origin, transformation):
    image = np.copy(image_origin)
    # apply pixel transformation 
    transformed_img = image # to change
    # Compute the similarity following the pixel-value transformation
    similarity_value = compute_similarity(image_origin,transformed_img)
    transformed_data = [transformed_img]
    # Apply affine transformation
    # apply translation here
    translated_img = transformed_img # to change
    transformed_data.append(translated_img)
    # apply scale change here
    scaled_img = transformed_img # to change
    transformed_data.append(scaled_img)
    # apply rotation here
    rotated_img = transformed_img # to change
    transformed_data.append(rotated_img)
    # apply shearing here
    sheared_img = transformed_img # to change
    transformed_data.append(sheared_img)            
    return transformed_data, similarity_value


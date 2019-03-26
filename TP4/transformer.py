import numpy as np
from skimage.measure import compare_ssim as ssim

def rotate(img, rad_angle):
    # to complete
    pass

def translate(img, trans_x, trans_y):
    # to complete
    pass

def scale(img, scale_1, scale_2):
    # to complete 
    pass

def shear(img, factor):
    # to complete 
    pass
    
def blur(img, sigma):
    # to complete
    pass

def change_brightness(img, factor):
    # to complete
    pass

def change_contrast(img, factor):
    # to complete 
    pass

def change_sharpness(img, factor):
    # to complete
    pass

def add_random_noise(img, delta, proba):
    # to complete
    pass

def build_random_transformation():
    # to complete
    pass

def compute_similarity(first_img, second_ing):
    # to complete
    pass 

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


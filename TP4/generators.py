import numpy as np
import transformer as trans
import matplotlib
import os

class Generator(object):

    def __init__(self, tf_session, tf_model, sim_threshold):
        self.session = tf_session
        self.model = tf_model
        self.sim_threshold = sim_threshold
        self.adv_found = 0
        self.gen_examples = 0
        self.original_data = None
        self.target_class = None
    
    def run_test(self, candidate):
        transformed_data, similarity_measure = trans.apply_transformation(self.original_data, candidate)
        generated_data = np.array(transformed_data)
        normalized_data = self.standardize(generated_data)
        self.gen_examples += generated_data.shape[0]
        if similarity_measure > self.sim_threshold:
            onehot_labels = np.zeros((generated_data.shape[0],10))
            onehot_labels[:,self.target_class] = 1
            logits = self.session.run(self.model.logits, feed_dict={self.model.features: normalized_data, self.model.labels: onehot_labels})
            self.follow_up_test(generated_data, logits)

    def follow_up_test(self, generated_data, logits):
        # to complete 
        # if test fails, store the resulting image
        # self.store_data(image, predicted_class)
        pass

    def get_rand_candidate(self):
        return trans.build_random_transformation()

    def store_data(self, image, predicted_class):
        self.adv_found += 1
        if not os.path.isdir('./test_images'):
            os.mkdir('./test_images')
        matplotlib.image.imsave("./test_images/id_{}_class_{}.png".format(self.adv_found,predicted_class), image)
    
    def standardize(self, features):
        axis = (0, 1, 2) if len(features.shape)==3 else (0, 1)
        mean_pixel = features.mean(axis=axis, keepdims=True)
        std_pixel = features.std(axis=axis, keepdims=True)
        return np.float32((features - mean_pixel) / std_pixel)
    
    def run(self, image, curr_target, max_iterations):
        self.original_data = image
        self.target_class = curr_target
        for _ in range(max_iterations):
            tr_candidate = self.get_rand_candidate()
            self.run_test(tr_candidate)
        print('{} / {}'.format(self.adv_found,self.gen_examples))

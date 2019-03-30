from dataset import Dataset
import tensorflow as tf
import numpy as np
import os
import learners
import argparse
import matplotlib
import generators


def test_model(checkpoint_path, model, n_elements, max_iterations, testing_dataset, sim_threshold):
    x_test, y_test = testing_dataset
    test_data = Dataset(x_test, y_test)
    sample_features, sample_labels = test_data.get_sample(sample_size=n_elements)
    prep_sample_features = test_data.standardize(sample_features)
    prep_sample_labels = test_data.get_one_hot_encoding(sample_labels)
    saver = tf.train.Saver()
    with tf.Session() as sess:
        saver.restore(sess, checkpoint_path)
        test_accurary, test_prediction  = sess.run([model.accuracy, model.correct_prediction], feed_dict={model.features: prep_sample_features, model.labels: prep_sample_labels})
        print('test accurary: {}'.format(test_accurary))
        selected_sample_features = sample_features[test_prediction]
        selected_sample_labels = sample_labels[test_prediction]
        sample_size = np.sum(test_prediction)
        generator = generators.Generator(sess, model, sim_threshold)
        for i in range(sample_size):
            print('running the example {}/{}'.format(i,sample_size))
            image = selected_sample_features[i]
            curr_target = selected_sample_labels[i]
            generator.run(image, curr_target, max_iterations)

if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument('--n', help='help')
    parser.add_argument('--max', help='help')
    args = vars(parser.parse_args())
    n_elements = 1000#int(args['n'])
    max_iterations = 10#int(args['max'])
    results_path = '../results'
    if not os.path.isdir(results_path):
        os.mkdir(results_path)
    (x_train, y_train), (x_test, y_test) = tf.keras.datasets.mnist.load_data()
    #learner parameters are inserted randomly to not have exceptions, but the model will be loaded from the checkpoint file
    model = learners.LeNet(lr=0.01,w_decay=0.0,keep_p=0.5)
    checkpoint_path = "./backup.ckpt"
    sim_threshold = 0.1#...#to prefix
    test_model(checkpoint_path=checkpoint_path, model=model, n_elements=n_elements, max_iterations=max_iterations, testing_dataset=(x_test, y_test), sim_threshold=sim_threshold)

import React from 'react';
import { View, Text, TouchableOpacity, StyleSheet, Alert } from 'react-native';
import { NativeModules } from 'react-native';

const { EsewaModule } = NativeModules;

const HomeScreen = () => {
  const handleEsewaPayment = async () => {
    try {
      const result = await EsewaModule.startEsewaPayment(
        '100.0',                     // amounthhjjhjhkkkkk
        'Test Payment',              // product name
        'prod123',                   // product ID
        'https://yourdomain.com/callback' // callback URL
      );
      console.log('✅ Payment Success:', result);
      Alert.alert('Payment Successful', result);
    } catch (e: any) {
      console.error('❌ Payment Failed:', e);
      Alert.alert('Payment Failed', e.message || 'Something went wrong');
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>eSewa Payment</Text>
      <TouchableOpacity style={styles.button} onPress={handleEsewaPayment}>
        <Text style={styles.buttonText}>Pay with eSewa</Text>
      </TouchableOpacity>
    </View>
  );
};

export default HomeScreen;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 16,
    backgroundColor: '#f4f4f4',
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 32,
  },
  button: {
    backgroundColor: '#61b33e',
    paddingHorizontal: 24,
    paddingVertical: 14,
    borderRadius: 8,
  },
  buttonText: {
    color: '#fff',
    fontSize: 18,
  },
});

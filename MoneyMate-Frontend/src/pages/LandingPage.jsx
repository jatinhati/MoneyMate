import React, { useRef } from 'react';
import { motion, useScroll, useTransform } from 'framer-motion';
import { TrendingUp, PieChart, Shield, Zap, Target, ArrowRight } from 'lucide-react';
import { Link } from 'react-router-dom';
import './LandingPage.css';

const LandingPage = () => {
    const containerRef = useRef(null);

    const features = [
        {
            icon: <TrendingUp className="w-8 h-8 text-yellow-400" />,
            title: "AI Insights",
            description: "Get smart suggestions to optimize your daily spending habits."
        },
        {
            icon: <PieChart className="w-8 h-8 text-amber-500" />,
            title: "Clear Visualization",
            description: "Track your growth with interactive charts and real-time data."
        },
        {
            icon: <Target className="w-8 h-8 text-yellow-500" />,
            title: "Budget Goals",
            description: "Set and achieve financial milestones with precision tracking."
        },
        {
            icon: <Shield className="w-8 h-8 text-orange-400" />,
            title: "Secure & Private",
            description: "Your financial data is encrypted and always under your control."
        }
    ];

    return (
        <div className="landing-container" ref={containerRef}>
            <div className="fluid-background"></div>

            <nav className="pill-nav">
                <div className="nav-logo">
                    <span className="logo-icon">M</span>
                    <span className="logo-text">MoneyMate</span>
                </div>
                <div className="nav-items">
                    <Link to="/" className="nav-link">Home</Link>
                    <a href="#features" className="nav-link">Features</a>
                    <Link to="/about" className="nav-link">How it works</Link>
                    <Link to="/pricing" className="nav-link">Pricing</Link>
                </div>
                <div className="nav-auth">
                    <Link to="/login" className="nav-link">Log in</Link>
                    <Link to="/signup" className="btn-pill-white">Get Started</Link>
                </div>
            </nav>

            <section className="hero-new">
                <motion.div
                    className="hero-content-centered"
                    initial={{ opacity: 0, y: 30 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.8 }}
                >
                    <div className="ai-pill">
                        <div className="dot-green"></div>
                        <span>AI FINANCE 2.0</span>
                    </div>

                    <h1 className="hero-title-large">
                        Elevate Your <br />
                        Wealth Potential.
                    </h1>

                    <p className="hero-description-centered">
                        Experience the future of financial tracking. Create stunning visualizations,
                        optimize analytics, and grow your assets in seconds with our advanced AI engine.
                    </p>

                    <div className="hero-actions-centered">
                        <Link to="/signup" className="btn-large-white">
                            Start Tracking Free
                        </Link>
                        <a href="#features" className="btn-large-glass">
                            View Features
                        </a>
                    </div>

                    <div className="trust-badge">
                        <p>TRUSTED BY 10,000+ CREATORS WORLDWIDE</p>
                        <div className="scroll-indicator">
                            <div className="mouse-wheel"></div>
                        </div>
                    </div>
                </motion.div>
            </section>

            <section id="features" className="features-new">
                {/* Keeping existing features grid but wrapped in new style if needed */}
                <div className="section-header">
                    <h2>Why Choose MoneyMate?</h2>
                    <p>The smartest way to manage your expenses.</p>
                </div>
                <div className="features-grid">
                    {features.map((feature, index) => (
                        <motion.div
                            key={index}
                            className="feature-card glass-card"
                            whileHover={{ y: -10 }}
                            initial={{ opacity: 0, y: 20 }}
                            whileInView={{ opacity: 1, y: 0 }}
                            transition={{ delay: index * 0.1 }}
                        >
                            <div className="feature-icon">{feature.icon}</div>
                            <h3>{feature.title}</h3>
                            <p>{feature.description}</p>
                        </motion.div>
                    ))}
                </div>
            </section>

            <footer className="landing-footer">
                <p>&copy; 2026 MoneyMate. All rights reserved.</p>
            </footer>
        </div>
    );
};

export default LandingPage;
